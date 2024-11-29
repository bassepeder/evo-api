package com.basse.api.responses.location

import kotlinx.serialization.Serializable

@Serializable
data class LocationStatisticsTimeline(
    val id: String,
    val name: String,
    val intervals: List<LocationStatisticsTimelineInterval>,
)

@Serializable
data class LocationStatisticsTimelineInterval(
    val name: String,
    val begin: Int,
    val end: Int,
    val maxCapacity: Int,
    val onsiteMinutes: Int,
    val percentageUsed: Double,
    val status: String,
)