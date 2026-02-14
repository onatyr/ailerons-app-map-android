package fr.ailerons.map.data.dtos

import fr.ailerons.map.data.entities.Individual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class IndividualDto(
    @SerialName("id")
    val id: Int,

    @SerialName("created_at")
    val createdAt: String,

    @SerialName("individual_name")
    val individualName: String,

    @SerialName("sex")
    val sex: String,

    @SerialName("common_name")
    val commonName: String,

    @SerialName("binomial_name")
    val binomialName: String,

    @SerialName("description")
    val description: String,
) {
    fun toIndividual(individualContext: IndividualContextDto): Individual {
        return Individual(
            id = id,
            individualName = individualName,
            sex = sex,
            commonName = commonName,
            binomialName = binomialName,
            description = description,
            situation = individualContext.situation,
            size = individualContext.size,
            behavior = individualContext.behavior
        )
    }
}