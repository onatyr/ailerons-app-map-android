package fr.ailerons.map.data.repositories

import fr.ailerons.map.data.dtos.IndividualContextDto
import fr.ailerons.map.data.dtos.IndividualDto
import fr.ailerons.map.data.entities.Individual
import fr.ailerons.map.data.entities.IndividualDAO
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
            val individualResponse = supabaseClient.from("individual")
                .select().data
            val individualContextResponse = supabaseClient.from("context")
                .select().data

            val json = Json { ignoreUnknownKeys = true }
            val individualList = json.decodeFromString<List<IndividualDto>>(individualResponse)
            val individualContextList =
                json.decodeFromString<List<IndividualContextDto>>(individualContextResponse)

            clearIndividual()
            individualList.forEach { individualDto ->
                insertIndividual(individualDto.toIndividual(
                    individualContextList.single { individualContextDto ->
                        individualContextDto.individualId == individualDto.id
                    }
                ))
            }
        }
    }

    fun getListIndividual() = individualDao.getAll()

    private suspend fun insertIndividual(individual: Individual) = individualDao.insert(individual)

    private suspend fun clearIndividual() = individualDao.deleteAll()
}