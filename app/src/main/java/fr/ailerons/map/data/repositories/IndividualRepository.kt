package fr.ailerons.map.data.repositories

import fr.ailerons.map.data.api.CoreApi
import fr.ailerons.map.data.entities.Individual
import fr.ailerons.map.data.entities.IndividualDAO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IndividualRepository @Inject constructor(
    private val coreApi: CoreApi,
    private val individualDao: IndividualDAO
) {
    suspend fun fetchFromRemote() = withContext(Dispatchers.IO) {
        val individuals = coreApi.getIndividuals().getOrElse { emptyList() }

        clearIndividual()
        individuals.forEach { individualDto ->
            insertIndividual(individualDto.toIndividual())
        }
    }

    fun getAll() = individualDao.getAll()

    fun getAllIds() = individualDao.getAllIds()

    fun getAllWithNonEmptyRecordPoints() = individualDao.getAllWithNonEmptyRecordPoints()

    fun getById(id: Int) = individualDao.getById(id)

    fun getColors() = individualDao.getColors()

    private suspend fun insertIndividual(individual: Individual) = individualDao.insert(individual)

    private suspend fun clearIndividual() = individualDao.deleteAll()
}