package com.basse.api.requests

import kotlinx.serialization.Serializable

@Serializable
data class AuthenticateUserRequest(
    val username: String,
    val password: String,
)
