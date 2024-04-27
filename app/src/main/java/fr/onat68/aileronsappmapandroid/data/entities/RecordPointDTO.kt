package fr.onat68.aileronsappmapandroid.data.entities

import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Contextual
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
    val depth: Int,

    @SerialName("csv_uuid")
    val csvUuid: String,
) {
    fun toRecordPointEntity(): RecordPoint {
        return RecordPoint(
            id = this.id,
            longitude = this.longitude,
            latitude = this.latitude,
            individualId = this.individualId,
            recordTimestamp = this.recordTimestamp,
            depth = this.depth
        )
    }
}