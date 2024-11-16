package com.basse.api.responses

import kotlinx.serialization.Serializable

@Serializable
data class MembershipDetailsResponse(
    val userId: String,
    val membershipNumber: Long,
    val membershipStatus: MembershipStatus,
    val profile: ProfileDetails,
    val keys: List<MembershipKey>,
    val productDetails: ProductDetails,
)

@Serializable
data class ProductDetails(
    val postSignupPresentation: String,
    val requiresPhoneVerification: Boolean,
)

@Serializable
enum class MembershipStatus {
    Presale,
    InTrial,
    Active,
    Freezed,
    PendingCancellation,
    Cancelled,
    CancelledInPresale,
    CancelledInTrial,
    Stopped,
    Unknown;

    companion object {
        fun fromString(value: String): MembershipStatus = when (value) {
            "presale" -> Presale
            "in_trial" -> InTrial
            "active" -> Active
            "in_freeze" -> Freezed
            "in_cancellation" -> PendingCancellation
            "cancelled" -> Cancelled
            "cancelled_in_presale" -> CancelledInPresale
            "cancelled_in_trial" -> CancelledInTrial
            "stopped" -> Stopped
            else -> Unknown
        }
    }
}

@Serializable
data class MembershipKey(
    val id: String,
    val code: String,
    val type: KeyType,
    val status: KeyStatus,
    val createdAt: String,
    val validFrom: String,
    val validTo: String?,
) {
    @Serializable
    enum class KeyType {
        Rfid,
        PinCode,
        Unknown;

        companion object {
            fun fromString(value: String): KeyType = when (value) {
                "rfid" -> Rfid
                "pin" -> PinCode
                else -> Unknown
            }
        }
    }

    @Serializable
    enum class KeyStatus {
        Inactive,
        Active,
        Unknown;

        companion object {
            fun fromString(value: String): KeyStatus = when (value) {
                "inactive" -> Inactive
                "active" -> Active
                else -> Unknown
            }
        }
    }
}

@Serializable
data class ProfileDetails(
    val name: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val address: ProfileAddress,
    val mobile: MobileDetails,
)

@Serializable
data class ProfileAddress(
    val street: String,
    val postalCode: String,
    val postalLocation: String,
)

@Serializable
data class MobileDetails(
    val number: String,
    val prefix: String,
)