package com.basse.api.responses.location

import com.basse.api.external.responses.EvoLocationStatisticsTimeline
import kotlinx.serialization.Serializable

@Serializable
data class LocationStatisticsTimeline(
    val id: String,
    val name: String,
    val intervals: List<LocationStatisticsTimelineInterval>,
) {
    companion object {
        fun fromExternalResponse(data: EvoLocationStatisticsTimeline): LocationStatisticsTimeline =
            LocationStatisticsTimeline(
                id = data.id,
                name = data.name,
                intervals = data.intervals.map { i ->
                    LocationStatisticsTimelineInterval(
                        name = i.name,
                        begin = i.begin,
                        end = i.end,
                        maxCapacity = i.maxCapacity,
                        percentageUsed = i.percentageUsed,
                        status = i.status,
                    )
                }
            )
    }
}

@Serializable
data class LocationStatisticsTimelineInterval(
    val name: String,
    val begin: Int,
    val end: Int,
    val maxCapacity: Int,
    val percentageUsed: Double,
    val status: String,
)