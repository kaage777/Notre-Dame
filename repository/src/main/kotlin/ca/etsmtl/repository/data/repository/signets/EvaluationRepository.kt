package ca.etsmtl.repository.data.repository.signets

import android.arch.lifecycle.LiveData
import ca.etsmtl.repository.AppExecutors
import ca.etsmtl.repository.data.api.ApiResponse
import ca.etsmtl.repository.data.api.SignetsApi
import ca.etsmtl.repository.data.db.dao.signets.EvaluationDao
import ca.etsmtl.repository.data.db.dao.signets.SommaireElementsEvaluationDao
import ca.etsmtl.repository.data.model.Resource
import ca.etsmtl.repository.data.model.signets.Cours
import ca.etsmtl.repository.data.model.signets.Evaluation
import ca.etsmtl.repository.data.model.signets.ListeDesElementsEvaluation
import ca.etsmtl.repository.data.model.signets.SignetsModel
import ca.etsmtl.repository.data.model.signets.SignetsUserCredentials
import ca.etsmtl.repository.data.model.signets.SommaireElementsEvaluation
import ca.etsmtl.repository.data.repository.NetworkBoundResource
import javax.inject.Inject

/**
 * This repository provide a list of the the student's evaluations (exams, assignments, etc.) for a
 * given course. It can also provide a summary for a given course.
 *
 * Created by Sonphil on 26-05-18.
 */

class EvaluationRepository @Inject constructor(
    appExecutors: AppExecutors,
    private val api: SignetsApi,
    private val evaluationDao: EvaluationDao,
    private val sommaireElementsEvaluationDao: SommaireElementsEvaluationDao
) : SignetsRepository(appExecutors) {

    /**
     * Returns a [List] of the the student's [Evaluation]s (exams, assignments, etc.) for a given
     * [Cours]
     *
     * @param userCredentials The user's credentials
     * @param cours The course
     * @param shouldFetch True if the data should be fetched from the network. False if the the data
     * should only be fetched from the DB.
     */
    fun getEvaluations(
        userCredentials: SignetsUserCredentials,
        cours: Cours,
        shouldFetch: Boolean
    ): LiveData<Resource<List<Evaluation>>> = object : NetworkBoundResource<List<Evaluation>, SignetsModel<ListeDesElementsEvaluation>>(appExecutors) {
        override fun saveCallResult(item: SignetsModel<ListeDesElementsEvaluation>) {
            item.data?.liste?.let { evaluationDao.insertAll(*it.toTypedArray()) }
        }

        override fun shouldFetch(data: List<Evaluation>?): Boolean = shouldFetch

        override fun loadFromDb(): LiveData<List<Evaluation>> {
            TODO()
        }

        override fun createCall(): LiveData<ApiResponse<SignetsModel<ListeDesElementsEvaluation>>> {
            return transformApiLiveData(api.listeDesElementsEvaluation(
                    userCredentials.codeAccesUniversel,
                    userCredentials.motPasse,
                    cours.sigle,
                    cours.groupe,
                    cours.session
            ))
        }
    }.asLiveData()

    /**
     * Returns a summary of the [Evaluation]s for a given [Cours]
     *
     * @param userCredentials The user's credentials
     * @param cours The course
     * @param shouldFetch True if the data should be fetched from the network. False if the the data
     * should only be fetched from the DB.
     */
    fun getEvaluationsSummary(
        userCredentials: SignetsUserCredentials,
        cours: Cours,
        shouldFetch: Boolean
    ): LiveData<Resource<SommaireElementsEvaluation>> = object : NetworkBoundResource<SommaireElementsEvaluation, SignetsModel<ListeDesElementsEvaluation>>(appExecutors) {
        override fun saveCallResult(item: SignetsModel<ListeDesElementsEvaluation>) {
            item.data?.let {
                with(SommaireElementsEvaluation(
                        cours.sigle,
                        cours.session,
                        it.noteACeJour,
                        it.scoreFinalSur100,
                        it.moyenneClasse,
                        it.ecartTypeClasse,
                        it.medianeClasse,
                        it.rangCentileClasse,
                        it.noteACeJourElementsIndividuels,
                        it.noteSur100PourElementsIndividuels
                )) {
                    sommaireElementsEvaluationDao.insert(this)
                }
            }
        }

        override fun shouldFetch(data: SommaireElementsEvaluation?): Boolean = shouldFetch

        override fun loadFromDb(): LiveData<SommaireElementsEvaluation> {
            TODO()
        }

        override fun createCall(): LiveData<ApiResponse<SignetsModel<ListeDesElementsEvaluation>>> {
            return transformApiLiveData(api.listeDesElementsEvaluation(
                    userCredentials.codeAccesUniversel,
                    userCredentials.motPasse,
                    cours.sigle,
                    cours.groupe,
                    cours.session
            ))
        }
    }.asLiveData()
}