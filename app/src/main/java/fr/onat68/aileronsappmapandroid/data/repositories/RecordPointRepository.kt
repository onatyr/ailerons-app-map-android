package fr.onat68.aileronsappmapandroid.data.repositories

import androidx.compose.runtime.asIntState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import fr.onat68.aileronsappmapandroid.data.entities.IndividualDAO
import fr.onat68.aileronsappmapandroid.data.entities.RecordPoint
import fr.onat68.aileronsappmapandroid.data.entities.RecordPointDAO
import fr.onat68.aileronsappmapandroid.data.entities.RecordPointDTO
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecordPointRepository @Inject constructor(
    private val supabaseClient: SupabaseClient,
    private val recordPointDao: RecordPointDAO
) {
    fun fetchListRecordPoint() {
        CoroutineScope(Dispatchers.IO).launch {
            val recordPoints = supabaseClient.from("record")
                .select().decodeList<RecordPointDTO>().sortedBy { it.recordTimestamp }
                .map { it.toRecordPointEntity() }

            clearRecordPoint()
            recordPointDao.insertAll(recordPoints)
        }
    }

    fun getListRecordPoint() = recordPointDao.getAll()

    private suspend fun insertRecordPoint(recordPoint: RecordPoint) =
        recordPointDao.insert(recordPoint)

    private suspend fun clearRecordPoint() = recordPointDao.deleteAll()
}