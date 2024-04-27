package fr.onat68.aileronsappmapandroid.data.repositories

import fr.onat68.aileronsappmapandroid.data.entities.IndividualDAO
import fr.onat68.aileronsappmapandroid.data.entities.RecordPoint
import fr.onat68.aileronsappmapandroid.data.entities.RecordPointDAO
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecordPointRepository @Inject constructor(private val recordPointDao: RecordPointDAO) {

    fun getListRecordPoint() = recordPointDao.getAll()

    suspend fun insertRecordPoint(recordPoint: RecordPoint) = recordPointDao.insert(recordPoint)

    suspend fun clearRecordPoint() = recordPointDao.deleteAll()
}