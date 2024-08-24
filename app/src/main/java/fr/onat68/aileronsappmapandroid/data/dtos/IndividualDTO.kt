package fr.onat68.aileronsappmapandroid.data.dtos

import fr.onat68.aileronsappmapandroid.data.entities.Individual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class IndividualDTO(
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
    fun toIndividualEntity(individualContext: IndividualContextDTO): Individual {
        return Individual(
            id = this.id,
            individualName = this.individualName,
            sex = this.sex,
            commonName = this.commonName,
            binomialName = this.binomialName,
            description = this.description,
            situation = individualContext.situation,
            size = individualContext.size,
            behavior = individualContext.behavior
        )
    }
}