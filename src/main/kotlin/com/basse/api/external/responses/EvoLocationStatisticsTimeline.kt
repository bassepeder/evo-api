package com.basse.api.external.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EvoLocationStatisticsTimeline(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String,
    @SerialName("intervals") val intervals: List<EvoLocationStatisticsTimelineInterval>,
)

@Serializable
data class EvoLocationStatisticsTimelineInterval(
    @SerialName("name") val name: String,
    @SerialName("begin") val begin: Int,
    @SerialName("end") val end: Int,
    @SerialName("maxCapacity") val maxCapacity: Int,
    @SerialName("percentageUsed") val percentageUsed: Double,
    @SerialName("status") val status: String,
)