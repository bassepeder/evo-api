package com.basse.api.external.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EvoApiErrorResponse(
    @SerialName("status") val status: String,
    @SerialName("reason") val reason: String
)