package fr.ailerons.map.data.dtos

import fr.ailerons.map.data.entities.RecordPoint
import kotlinx.serialization.Serializable

@Serializable
data class RecordPointDto(
    val longitude: Float,
    val latitude: Float,
    val idIndividual: Int,
    val recordTimestamp: String,
    val depth: Int?,
) {
    fun toRecordPoint() = RecordPoint(
        longitude = if (idIndividual == 18) longitude + 0.3f else longitude, // todo remove
        latitude = if (idIndividual == 18) latitude + 0.3f else latitude, // todo remove
        idIndividual = idIndividual,
        recordTimestamp = recordTimestamp,
        depth = depth,
    )
}
