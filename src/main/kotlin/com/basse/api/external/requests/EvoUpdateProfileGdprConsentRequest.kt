package com.basse.api.external.requests

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EvoUpdateProfileGdprConsentRequest(
    @SerialName("gdpr_opt_in") val optIn: Boolean,
)