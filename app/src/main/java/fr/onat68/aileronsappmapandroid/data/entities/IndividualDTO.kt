package fr.onat68.aileronsappmapandroid.data.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class IndividualDTO(
    @SerialName("id")
    val id: Int,

    @SerialName("created_at")
    val createdAt: String,

    @SerialName("name")
    val name: String,

    @SerialName("sex")
    val sex: String,

    @SerialName("pictures")
    val pictures: List<String>?,

    @SerialName("common_name")
    val commonName: String,

    @SerialName("binomial_name")
    val binomialName: String,

    @SerialName("description")
    val description: String,

    @SerialName("icon")
    val icon: String
) {
    fun toIndividualEntity(): Individual {
        return Individual(
            id = this.id,
            name = this.name,
            sex = this.sex,
            pictures = this.pictures,
            commonName = this.commonName,
            binomialName = this.binomialName,
            description = this.description,
            icon = this.icon
        )
    }
}