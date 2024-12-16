package com.basse.api.requests

import com.basse.api.external.requests.EvoUpdatePrimaryProfileLocationRequest
import kotlinx.serialization.Serializable

@Serializable
data class UpdatePrimaryLocationRequest(val locationId: String) {
    fun toExternalRequest(): EvoUpdatePrimaryProfileLocationRequest = EvoUpdatePrimaryProfileLocationRequest(locationId)
}