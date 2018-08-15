package ca.etsmtl.repository.data.api.requestbody.signets

/**
 * Created by Sonphil on 12-08-18.
 */

data class ListeDesSeancesRequestBody(
    /** The student's universal code **/
    val codeAccesUniversel: String,
    /** The student's password **/
    val motPasse: String,
    /** The group number **/
    val pSigle: String,
    /** The session **/
    val pSession: String,
    /** The starting date **/
    val pDateDebut: String,
    /** The end date **/
    val pDateFin: String
)