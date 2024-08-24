package fr.onat68.aileronsappmapandroid.data.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class IndividualContextDTO(
    @SerialName("individual_id")
    val individualId: Int,

    @SerialName("situation")
    val situation: String,

    @SerialName("size")
    val size: Int,

    @SerialName("behavior")
    val behavior: String
)