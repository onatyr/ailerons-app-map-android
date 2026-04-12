package fr.ailerons.map.data.repositories

import fr.ailerons.map.data.api.CoreApi
import fr.ailerons.map.data.entities.RecordPointDAO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecordPointRepository @Inject constructor(
    private val coreApi: CoreApi,
    private val recordPointDao: RecordPointDAO
) {

    suspend fun fetchFromRemote() = withContext(Dispatchers.IO) {
        val recordPoints = coreApi.getRecordPoints().getOrElse { emptyList() } // todo check if the idIndividual is in the db
            .sortedBy { it.recordTimestamp }
            .map { it.toRecordPoint() }

        clearRecordPoint()
        recordPointDao.insertAll(recordPoints)
    }

    fun getWithColorByIds(ids: List<Int>) = recordPointDao.getWithColorByIds(ids)

    private suspend fun clearRecordPoint() = recordPointDao.deleteAll()
}