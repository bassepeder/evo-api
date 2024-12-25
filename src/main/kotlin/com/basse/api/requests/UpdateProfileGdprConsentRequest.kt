package com.basse.api.requests

import kotlinx.serialization.Serializable

@Serializable
data class UpdateProfileGdprConsentRequest(val optIn: Boolean)
