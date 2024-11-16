package com.basse.api.external

import com.basse.api.external.responses.EvoAuthenticateUserResponse
import com.basse.api.responses.Invoice
import com.basse.api.responses.MemberWorkouts
import com.basse.api.responses.MembershipDetailsResponse
import com.basse.api.responses.NextInvoice

interface EvoApiService {
    suspend fun authenticateUser(
        username: String,
        password: String,
    ): Result<EvoAuthenticateUserResponse>

    suspend fun getMembershipDetails(token: String): Result<MembershipDetailsResponse>

    suspend fun getInvoices(token: String): List<Invoice>
    suspend fun getNextInvoice(token: String): NextInvoice

    suspend fun getWorkoutsForMember(token: String): MemberWorkouts
}

