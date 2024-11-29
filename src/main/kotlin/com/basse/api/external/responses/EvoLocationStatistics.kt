package com.basse.api.external.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EvoLocationStatistics(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String,
    @SerialName("current") val current: Int,
    @SerialName("max_capacity") val maxCapacity: Int,
    @SerialName("percentageUsed") val percentageUsed: Double,
)
