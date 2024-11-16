package com.basse.api.responses

import kotlinx.serialization.Serializable

@Serializable
data class AuthenticateUserResponse(val token: String)
