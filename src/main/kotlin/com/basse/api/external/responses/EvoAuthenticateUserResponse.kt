package com.basse.api.external.responses

import kotlinx.serialization.Serializable

@Serializable
data class EvoAuthenticateUserResponse(val token: String)
