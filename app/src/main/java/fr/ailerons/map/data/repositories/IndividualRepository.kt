package fr.ailerons.map.data.repositories

import fr.ailerons.map.data.dtos.IndividualContextDto
import fr.ailerons.map.data.dtos.IndividualDto
import fr.ailerons.map.data.entities.Individual
import fr.ailerons.map.data.entities.IndividualDAO
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IndividualRepository @Inject constructor(
    private val supabaseClient: SupabaseClient,
    private val individualDao: IndividualDAO
) {
    suspend fun fetchFromRemote() = withContext(Dispatchers.IO) {
        val individuals = supabaseClient.from("individual")
            .select().decodeList<IndividualDto>()
        val contexts = supabaseClient.from("context")
            .select().decodeList<IndividualContextDto>()

        clearIndividual()
        individuals.forEach { individualDto ->
            insertIndividual(
                individualDto.toIndividual(
                    contexts.single { contextDto ->
                        contextDto.individualId == individualDto.id
                    }
                ))
        }
    }

    fun getAll() = individualDao.getAll()

    fun getById(id: Int) = individualDao.getById(id)

    fun getColors() = individualDao.getColors()

    private suspend fun insertIndividual(individual: Individual) = individualDao.insert(individual)

    private suspend fun clearIndividual() = individualDao.deleteAll()
}