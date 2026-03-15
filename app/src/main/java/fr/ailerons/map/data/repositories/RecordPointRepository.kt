package fr.ailerons.map.data.repositories

import fr.ailerons.map.data.dtos.RecordPointDto
import fr.ailerons.map.data.entities.RecordPointDAO
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.filter.FilterOperator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecordPointRepository @Inject constructor(
    private val supabaseClient: SupabaseClient,
    private val recordPointDao: RecordPointDAO
) {

    suspend fun fetchFromRemote() = withContext(Dispatchers.IO) {
        val recordPoints = supabaseClient.from("record")
            .select {
                filter {
                    filterNot("individual_id", FilterOperator.IS, null)
                }
            }
            .decodeList<RecordPointDto>()
            .sortedBy { it.recordTimestamp }
            .map { it.toRecordPoint() }

        clearRecordPoint()
        recordPointDao.insertAll(recordPoints)
    }

    fun getWithColorByIndividualId(id: Int) = recordPointDao.getWithColorByIndividualId(id)

    fun getAllWithColor() = recordPointDao.getAllWithColor()

    private suspend fun clearRecordPoint() = recordPointDao.deleteAll()
}