package fr.onat68.aileronsappmapandroid.data.dtos

import android.os.Build
import androidx.annotation.RequiresApi
import fr.onat68.aileronsappmapandroid.data.entities.RecordPoint
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecordPointDTO @RequiresApi(Build.VERSION_CODES.O) constructor(
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

    @SerialName("csv_uuid")
    val csvUuid: String,
) {
    fun toRecordPointEntity() = RecordPoint(
        id = id,
        longitude = longitude,
        latitude = latitude,
        individualId = individualId,
        recordTimestamp = recordTimestamp,
        depth = depth
    )
}
