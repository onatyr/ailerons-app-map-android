package fr.ailerons.map.data.repositories

import fr.ailerons.map.data.api.CoreApi
import fr.ailerons.map.data.entities.ArticleDAO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArticleRepository @Inject constructor(
    private val coreApi: CoreApi,
    private val articleDao: ArticleDAO
) {

    suspend fun fetchFromRemote() = withContext(Dispatchers.IO) {
        val articles = coreApi.getArticles().getOrElse { emptyList() }
            .sortedByDescending { LocalDateTime.parse(it.publicationDate) }
            .map { it.toArticle() }

        clearArticles()
        articleDao.insertAll(articles)
    }

    fun getListArticle() = articleDao.getAll()

    private suspend fun clearArticles() = articleDao.deleteAll()
}