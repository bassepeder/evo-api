package com.basse.api.responses.location

import kotlinx.serialization.Serializable

@Serializable
data class Location(
    val id: String,
    val name: String,
)
