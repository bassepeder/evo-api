package com.basse.api.responses

import kotlinx.serialization.Serializable

@Serializable
data class MemberWorkouts(
    val totalWorkouts: Int,
    val workoutMonths: List<WorkoutMonth>
)

@Serializable
data class WorkoutMonth(
    val totalWorkouts: Int,
    val year: Int,
    val month: Int
)