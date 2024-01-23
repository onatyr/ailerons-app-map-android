package fr.onat68.aileronsappmapandroid.species

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Individual(
    @SerialName("id")
    val id: Int,

    @SerialName("created_at")
    val createdAt: String,

    @SerialName("individual_name")
    val individualName: String,

    @SerialName("common_name")
    val commonName: String,

    @SerialName("binomial_name")
    val binomialName: String,

    @SerialName("age")
    val age: Int,

    @SerialName("size")
    val size: Int,

    @SerialName("weight")
    val weight: Int,

    @SerialName("sex")
    val sex: String,

    @SerialName("total_distance")
    val totalDistance: Int,

    @SerialName("description")
    val description: String,

    @SerialName("icon")
    val icon: Int,

    @SerialName("picture") //Should change
    val picture: String,

    @SerialName("individual_record_id")
    val individualRecordId: Int,

    @Contextual
    val liked: Boolean = false

)