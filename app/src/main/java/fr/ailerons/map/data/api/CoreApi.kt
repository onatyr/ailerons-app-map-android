package fr.ailerons.map.data.api

import android.util.Log
import fr.ailerons.map.data.dtos.ArticleDto
import fr.ailerons.map.data.dtos.IndividualDto
import fr.ailerons.map.data.dtos.RecordPointDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.path
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CoreApi @Inject constructor(
    baseClient: HttpClient
) {
    companion object {
        private const val TAG = "CoreApi"
    }

    private val httpClient = baseClient.config {
        defaultRequest {
            url("https://thirsty-marys-ailerons-75ee0bac.koyeb.app/")
            header("Content-Type", "application/json")
        }
    }

    suspend fun getIndividuals() = get<List<IndividualDto>>("data/individuals")

    suspend fun getRecordPoints() = get<List<RecordPointDto>>("data/records/point")

    suspend fun getArticles() = get<List<ArticleDto>>("data/articles")

    private suspend inline fun <reified T> get(path: String) =
        runCatching {
            httpClient.get { url { path(path) } }.body<T>()
        }.onFailure { Log.e(TAG, "Error while trying to get in CoreApi: ${it.message}") }
}
