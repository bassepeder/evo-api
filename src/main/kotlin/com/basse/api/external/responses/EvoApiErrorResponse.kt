package com.basse.api.external.responses

import kotlinx.serialization.Serializable

@Serializable
data class EvoApiErrorResponse(
    val status: String,
    val reason: String
)