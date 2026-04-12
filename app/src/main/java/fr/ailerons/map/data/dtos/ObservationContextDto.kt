package fr.ailerons.map.data.dtos

import kotlinx.serialization.Serializable

@Serializable
data class ObservationContextDto(
    val situation: String,
    val size: Double,
    val behavior: String
)