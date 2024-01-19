package fr.onat68.aileronsappmapandroid

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Record(
    @SerialName("id")
    val id: Int,

    @SerialName("created_at")
    val createdAt: String,

    @SerialName("longitude")
    val longitude: Float,

    @SerialName("latitude")
    val latitude: Float,

    @SerialName("individual_id")
    val individualId: Int,

    @SerialName("csv_id")
    val csvId: Int,

    @SerialName("record_timestamp")
    val recordTimestamp: String
)