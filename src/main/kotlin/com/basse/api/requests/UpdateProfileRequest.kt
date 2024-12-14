package com.basse.api.requests

import com.basse.api.external.requests.EvoUpdateProfileRequest
import com.basse.api.external.responses.EvoProfileDetails
import com.basse.api.responses.MobileDetails
import com.basse.api.responses.ProfileAddress
import kotlinx.serialization.Serializable

@Serializable
data class UpdateProfileRequest(
    val firstName: String,
    val lastName: String,
    val email: String,
    val address: ProfileAddress,
    val mobile: MobileDetails,
) {
    fun isValid(): Boolean = firstName.isNotBlank()
            && lastName.isNotBlank()
            && email.isNotBlank()
            && address.street.isNotBlank()
            && address.postalLocation.isNotBlank()
            && address.postalCode.isNotBlank()
            && mobile.number.isNotBlank()
            && mobile.prefix.isNotBlank()

    fun toExternalRequest(): EvoUpdateProfileRequest {
        return EvoUpdateProfileRequest(
            firstName = firstName,
            lastName = lastName,
            email = email,
            address = EvoProfileDetails.EvoProfileAddress(
                street = address.street,
                postalCode = address.postalCode,
                postalLocation = address.postalLocation,
            ),
            mobile = EvoProfileDetails.EvoMobileDetails(
                prefix = mobile.prefix,
                number = mobile.number,
            )
        )
    }
}
