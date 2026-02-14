package fr.ailerons.map.data.dtos

import fr.ailerons.map.data.entities.RecordPoint
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecordPointDto(
    @SerialName("id")
    val id: Int,

    @SerialName("created_at")
    val createdAt: String,

    @SerialName("longitude")
    val longitude: Float,

    @SerialName("latitude")
    val latitude: Float,

    @SerialName("individual_id")
    val individualId: Int?,

    @SerialName("record_timestamp")
    val recordTimestamp: String,

    @SerialName("depth")
    val depth: Int?,
) {
    fun toRecordPoint() = RecordPoint(
        id = id,
        longitude = longitude,
        latitude = latitude,
        individualId = individualId,
        recordTimestamp = recordTimestamp,
        depth = depth
    )
}
