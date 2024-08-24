package fr.onat68.aileronsappmapandroid.data.repositories

import fr.onat68.aileronsappmapandroid.data.dtos.ArticleDTO
import fr.onat68.aileronsappmapandroid.data.entities.ArticleDAO
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject

class ArticleRepository
@Inject constructor(
    private val supabaseClient: SupabaseClient,
    private val articleDao: ArticleDAO
) {
    init {
        fetchListNews()
    }

    private fun fetchListNews() {
        CoroutineScope(Dispatchers.IO).launch {
            val response = supabaseClient.from("article")
                .select()
                .data

            val json = Json { ignoreUnknownKeys = true }
            val articles = json.decodeFromString<List<ArticleDTO>>(response)
                .sortedBy { it.publicationDate }
                .map { it.toArticleEntity() }

            clearArticles()
            articleDao.insertAll(articles)
        }
    }

    fun getListArticle() = articleDao.getAll()

    private suspend fun clearArticles() = articleDao.deleteAll()
}