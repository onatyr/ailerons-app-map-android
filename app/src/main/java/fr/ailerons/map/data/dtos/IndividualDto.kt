package fr.ailerons.map.data.dtos

import fr.ailerons.map.data.entities.Individual
import kotlinx.serialization.Serializable
import kotlin.random.Random

@Serializable
data class IndividualDto(
    val id: Int,
    val createdAt: String,
    val individualName: String,
    val sex: String,
    val commonName: String,
    val binomialName: String,
    val observationContext: ObservationContextDto,
    val description: String,
) {
    fun toIndividual(): Individual {
        return Individual(
            id = id,
            individualName = individualName,
            sex = sex,
            commonName = commonName,
            binomialName = binomialName,
            description = description,
            situation = observationContext.situation,
            size = observationContext.size,
            behavior = observationContext.behavior,
            color = String.format(
                "#%02x%02x%02x",
                Random.nextInt(256),
                Random.nextInt(256),
                Random.nextInt(256)
            ) // todo remove
        )
    }
}