package com.basse.api.external

import com.basse.api.external.responses.EvoAuthenticateUserResponse
import com.basse.api.responses.MembershipDetailsResponse

interface EvoApiService {
    suspend fun authenticateUser(
        username: String,
        password: String,
    ): Result<EvoAuthenticateUserResponse>

    suspend fun getMembershipDetails(token: String): Result<MembershipDetailsResponse>
}

