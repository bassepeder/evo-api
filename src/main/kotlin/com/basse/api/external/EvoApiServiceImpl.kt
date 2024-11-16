package com.basse.api.external

import com.basse.Constants
import com.basse.api.external.responses.EvoAuthenticateUserResponse
import com.basse.api.responses.MembershipDetailsResponse
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
                    userId = details.userId,
                    membershipNumber = details.membershipNumber,
                    membershipStatus = MembershipStatus.fromString(details.status),
                    keys = details.keys.map { key -> key.toKey() },
                    profile = details.profile.toProfileDetails(),
                    productDetails = ProductDetails(
                        postSignupPresentation = details.productDetails.postSignupPresentation,
                        requiresPhoneVerification = details.productDetails.requirePhoneVerification,
                    )
                ))
            },
            onFailure = { exception -> Result.failure(exception) }
        )
    }
}