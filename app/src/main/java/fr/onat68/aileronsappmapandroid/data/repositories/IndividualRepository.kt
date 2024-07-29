package fr.onat68.aileronsappmapandroid.data.repositories

import android.util.Log
import fr.onat68.aileronsappmapandroid.data.entities.Individual
import fr.onat68.aileronsappmapandroid.data.entities.IndividualDAO
import fr.onat68.aileronsappmapandroid.data.entities.IndividualDTO
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IndividualRepository @Inject constructor(
    private val supabaseClient: SupabaseClient,
    private val individualDao: IndividualDAO
) {

    init {
        fetchListIndividual()
    }

    private fun fetchListIndividual() {
        CoroutineScope(Dispatchers.IO).launch {
            val response = supabaseClient.from("individual")
                .select().data
            val json = Json { ignoreUnknownKeys = true }
            val individualList = json.decodeFromString<List<IndividualDTO>>(response)

            clearIndividual()
            individualList.forEach { insertIndividual(it.toIndividualEntity()) }
        }
    }

    fun getListIndividual() = individualDao.getAll()

    private suspend fun insertIndividual(individual: Individual) = individualDao.insert(individual)

    private suspend fun clearIndividual() = individualDao.deleteAll()

    fun getListFavorite() = individualDao.getListFavorite()
    suspend fun addToFavorite(id: Int) = individualDao.addToFavorite(id)
    suspend fun removeFromFavorite(id: Int) = individualDao.removeFromFavorite(id)
}