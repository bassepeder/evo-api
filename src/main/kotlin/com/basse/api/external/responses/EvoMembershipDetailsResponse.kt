package com.basse.api.external.responses

import com.basse.api.responses.MembershipKey
import com.basse.api.responses.MobileDetails
import com.basse.api.responses.ProfileAddress
import com.basse.api.responses.ProfileDetails
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EvoMembershipDetailsResponse(
    @SerialName("id") val userId: String,
    @SerialName("membership_number") val membershipNumber: Long,
    @SerialName("profile") val profile: EvoProfileDetails,
    @SerialName("status") val status: String,
    @SerialName("keys") val keys: List<EvoMembershipKey>,
    @SerialName("product") val productDetails: EvoProductDetails,
)

@Serializable
data class EvoProfileDetails(
    @SerialName("name") val name: String,
    @SerialName("first_name") val firstName: String,
    @SerialName("last_name") val lastName: String,
    @SerialName("email") val email: String,
    @SerialName("address") val address: EvoProfileAddress,
    @SerialName("mobile") val mobile: EvoMobileDetails
) {
    fun toProfileDetails(): ProfileDetails {
        return ProfileDetails(
            name = name.trim(),
            firstName = firstName.trim(),
            lastName = lastName.trim(),
            email = email,
            address = ProfileAddress(
                street = address.street,
                postalCode = address.postalCode,
                postalLocation = address.postalLocation,
            ),
            mobile = MobileDetails(
                number = mobile.number,
                prefix = mobile.prefix,
            )
        )
    }

    @Serializable
    data class EvoProfileAddress(
        @SerialName("street") val street: String,
        @SerialName("postal_code") val postalCode: String,
        @SerialName("postal_location") val postalLocation: String
    )

    @Serializable
    data class EvoMobileDetails(
        @SerialName("number") val number: String,
        @SerialName("prefix") val prefix: String
    )
}

@Serializable
data class EvoMembershipKey(
    @SerialName("id") val id: String,
    @SerialName("code") val code: String,
    @SerialName("type") val type: String,
    @SerialName("status") val status: String,
    @SerialName("created_at") val createdAt: String,
    @SerialName("valid_from") val validFrom: String,
    @SerialName("valid_to") val validTo: String?,
) {
    fun toKey(): MembershipKey {
        return MembershipKey(
            id = id,
            code = code,
            type = MembershipKey.KeyType.fromString(type),
            status = MembershipKey.KeyStatus.fromString(status),
            createdAt = createdAt,
            validFrom = validFrom,
            validTo = validTo,
        )
    }
}

@Serializable
data class EvoProductDetails(
    @SerialName("post_signup_presentation") val postSignupPresentation: String,
    @SerialName("require_phone_verification") val requirePhoneVerification: Boolean,
)
