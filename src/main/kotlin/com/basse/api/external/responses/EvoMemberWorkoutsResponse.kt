package com.basse.api.external.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EvoMemberWorkoutsResponse(
    @SerialName("total") val totalWorkouts: Int,
    @SerialName("months") val workoutMonths: List<EvoWorkoutMonth>,
)

@Serializable
data class EvoWorkoutMonth(
    @SerialName("count") val totalWorkouts: Int,
    @SerialName("year") val year: Int,
    @SerialName("month") val month: Int,
)
