package fr.onat68.aileronsappmapandroid.data.repositories

import fr.onat68.aileronsappmapandroid.data.dtos.RecordPointDto
import fr.onat68.aileronsappmapandroid.data.entities.RecordPointDAO
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecordPointRepository @Inject constructor(
    private val supabaseClient: SupabaseClient,
    private val recordPointDao: RecordPointDAO
) {

    init {
        fetchListRecordPoint()
    }

    private fun fetchListRecordPoint() {
        CoroutineScope(Dispatchers.IO).launch {
            val response = supabaseClient.from("record")
                .select()
                .data

            val json = Json { ignoreUnknownKeys = true }
            val recordPoints = json.decodeFromString<List<RecordPointDto>>(response)
                .sortedBy { it.recordTimestamp }
                .map { it.toRecordPoint() }

            clearRecordPoint()
            recordPointDao.insertAll(recordPoints)
        }
    }

    fun getByIdIndividual(id: Int) = recordPointDao.getByIdIndividual(id)

    fun getAll() = recordPointDao.getAll()

    private suspend fun clearRecordPoint() = recordPointDao.deleteAll()
}