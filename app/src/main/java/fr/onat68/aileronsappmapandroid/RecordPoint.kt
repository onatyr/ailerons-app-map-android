package fr.onat68.aileronsappmapandroid

import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecordPoint @RequiresApi(Build.VERSION_CODES.O) constructor(
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
    val recordTimestampString: String,

    @Contextual
    val recordTimestamp: LocalDateTime = LocalDateTime.parse(recordTimestampString)
    )