package com.basse.api.responses.location

import kotlinx.serialization.Serializable

@Serializable
data class LocationStatistics(
    val id: String,
    val name: String,
    val current: Int,
    val maxCapacity: Int,
    val percentageUsed: Double,
)
