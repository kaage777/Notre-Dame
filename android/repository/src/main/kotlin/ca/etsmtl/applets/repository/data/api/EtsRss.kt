package ca.etsmtl.applets.repository.data.api

import com.prof.rssparser.Article
import com.prof.rssparser.Parser
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

/**
 * Created by Sonphil on 17-01-19.
 */

class EtsRss(private val context: CoroutineContext) {
    private val newsUrl = "https://www.etsmtl.ca/Nouvelles/rss"

    suspend fun etsNews(): Deferred<List<Article>> = CompletableDeferred<List<Article>>().apply {
        withContext(context) {
            try {
                val parser = Parser()
                val articleList = parser.getArticles(newsUrl)

                complete(articleList)
            } catch (e: Exception) {
                completeExceptionally(e)
            }
        }
    }
}