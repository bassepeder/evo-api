package com.basse.api.external.requests

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EvoAuthenticateUserRequest(
    @SerialName("operator_id") val operatorId: String,
    val username: String,
    val password: String,
)
