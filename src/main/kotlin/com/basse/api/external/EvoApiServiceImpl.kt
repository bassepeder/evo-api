package com.basse.api.external

import com.basse.Constants
import com.basse.api.external.responses.EvoAuthenticateUserResponse
import com.basse.api.responses.CurrentPaymentMethod
import com.basse.api.responses.LocationDetails
import com.basse.api.responses.MembershipDetails
import com.basse.api.responses.MembershipDetailsResponse
import com.basse.api.responses.MembershipFreeze
import com.basse.api.responses.MembershipStatus
import com.basse.api.responses.ProductDetails

class EvoApiServiceImpl(private val apiClient: EvoApiClient): EvoApiService {

    override suspend fun authenticateUser(username: String, password: String): Result<EvoAuthenticateUserResponse> =
        apiClient.authenticateUser(
            operatorId = Constants.OPERATOR_ID,
            username = username,
            password = password
        )

    override suspend fun getMembershipDetails(token: String): Result<MembershipDetailsResponse> {
        val result = apiClient.getMembershipDetails(token)
        return result.fold(
            onSuccess = { details ->
                Result.success(MembershipDetailsResponse(
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
                    referralCode = details.referralCode,
                    currentPaymentMethod = CurrentPaymentMethod(
                        id = details.currentPaymentMethod.id,
                        brand = details.currentPaymentMethod.brand,
                        details = details.currentPaymentMethod.details,
                    )
                ))
            },
            onFailure = { exception -> Result.failure(exception) }
        )
    }
}