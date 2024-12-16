package com.basse.api.external.requests

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EvoUpdatePrimaryProfileLocationRequest(@SerialName("location_id") val locationId: String)