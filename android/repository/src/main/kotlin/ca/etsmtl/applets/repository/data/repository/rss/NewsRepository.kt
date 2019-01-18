package ca.etsmtl.applets.repository.data.repository.rss

import ca.etsmtl.applets.repository.data.api.EtsRss
import ca.etsmtl.applets.repository.data.model.News
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import java.util.Date
import javax.inject.Inject

/**
 * Created by Sonphil on 17-01-19.
 */

class NewsRepository @Inject constructor(private val etsRss: EtsRss) {
    suspend fun getNews(): Deferred<List<News>> = CompletableDeferred<List<News>>().apply {
        try {
            val result = etsRss.etsNews().await().map {
                News("", "", "", Date(), "", Date())
            }
            // TODO: insert into DB
            complete(result)

        } catch (e: Exception) {
            completeExceptionally(e)
        }
    }
}