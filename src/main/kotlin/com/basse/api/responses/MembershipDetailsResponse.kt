package com.basse.api.responses

import com.basse.api.external.responses.EvoMembershipDetailsResponse
import kotlinx.serialization.Serializable

@Serializable
data class MembershipDetailsResponse(
    val membershipDetails: MembershipDetails,
    val profile: ProfileDetails,
    val keys: List<MembershipKey>,
    val product: ProductDetails,
    val location: LocationDetails,
    val referralCode: String,
    val locale: String,
    val gdprConsentGiven: Boolean,
    val currentPaymentMethod: CurrentPaymentMethod,
) {
    companion object {
        fun mapFromEvoResponse(details: EvoMembershipDetailsResponse): MembershipDetailsResponse {
            return MembershipDetailsResponse(
                membershipDetails = MembershipDetails(
                    id = details.membershipId,
                    number = details.membershipNumber,
                    status = MembershipStatus.fromString(details.status),
                    createdAt = details.membershipCreatedAt,
                    beganAt = details.membershipBeganAt,
                    endsAt = details.membershipEndsAt,
                    activatesOn = details.membershipActivatesOn,
                    freezes = details.freezePeriods?.map { period -> MembershipFreeze(
                        id = period.id,
                        startDate = period.beginDate,
                        endDate = period.endDate,
                        cancelledDate = period.cancelDate
                    )} ?: emptyList(),
                ),
                keys = details.keys.map { key -> key.toKey() },
                profile = details.profile.toProfileDetails(),
                product = ProductDetails(
                    postSignupPresentation = details.productDetails.postSignupPresentation,
                    requiresPhoneVerification = details.productDetails.requirePhoneVerification,
                ),
                location = LocationDetails(
                    id = details.locationDetails.id,
                    name = details.locationDetails.name,
                ),
                gdprConsentGiven = details.gdprOptIn,
                locale = details.locale,
                referralCode = details.referralCode,
                currentPaymentMethod = CurrentPaymentMethod(
                    id = details.currentPaymentMethod.id,
                    brand = details.currentPaymentMethod.brand,
                    details = details.currentPaymentMethod.details,
                )
            )
        }
    }
}

@Serializable
data class MembershipDetails(
    val id: String,
    val number: Long,
    val status: MembershipStatus,
    val createdAt: String,
    val beganAt: String,
    val endsAt: String?,
    val activatesOn: String?,
    val freezes: List<MembershipFreeze>
)

@Serializable
data class CurrentPaymentMethod(
    val id: String,
    val brand: String,
    val details: String,
)

@Serializable
data class MembershipFreeze(
    val id: String,
    val startDate: String,
    val endDate: String,
    val cancelledDate: String,
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
data class LocationDetails(
    val id: String,
    val name: String,
)

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