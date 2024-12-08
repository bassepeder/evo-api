package com.basse.api.external.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EvoExpiredTokenResponse(
    @SerialName("error") val errorCode: Int,
    @SerialName("reason") val reason: String
)