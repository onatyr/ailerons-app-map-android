package fr.ailerons.map.data.repositories

import fr.ailerons.map.data.dtos.ArticleDto
import fr.ailerons.map.data.entities.ArticleDAO
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArticleRepository @Inject constructor(
    private val supabaseClient: SupabaseClient,
    private val articleDao: ArticleDAO
) {

    suspend fun fetchFromRemote() = withContext(Dispatchers.IO) {
        val response = supabaseClient.from("article")
            .select()
            .data

        val json = Json { ignoreUnknownKeys = true }
        val articles = json.decodeFromString<List<ArticleDto>>(response)
            .sortedByDescending { LocalDateTime.parse(it.publicationDate) }
            .map { it.toArticle() }

        clearArticles()
        articleDao.insertAll(articles)
    }

    fun getListArticle() = articleDao.getAll()

    private suspend fun clearArticles() = articleDao.deleteAll()
}