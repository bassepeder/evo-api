package com.basse.api.external.requests

import com.basse.api.external.responses.EvoProfileDetails
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EvoUpdateProfileRequest(
    @SerialName("first_name") val firstName: String,
    @SerialName("last_name") val lastName: String,
    @SerialName("email") val email: String,
    @SerialName("address") val address: EvoProfileDetails.EvoProfileAddress,
    @SerialName("mobile") val mobile: EvoProfileDetails.EvoMobileDetails,
)