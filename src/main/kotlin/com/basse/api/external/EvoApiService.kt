package com.basse.api.external

import com.basse.api.external.responses.EvoAuthenticateUserResponse
import com.basse.api.responses.Invoice
import com.basse.api.responses.location.Location
import com.basse.api.responses.location.LocationStatistics
import com.basse.api.responses.MemberWorkouts
import com.basse.api.responses.MembershipDetailsResponse
import com.basse.api.responses.NextInvoice
import com.basse.api.responses.location.LocationStatisticsTimeline

interface EvoApiService {
    suspend fun authenticateUser(
        username: String,
        password: String,
    ): Result<EvoAuthenticateUserResponse>

    suspend fun getMembershipDetails(token: String): MembershipDetailsResponse

    suspend fun getInvoices(token: String): List<Invoice>
    suspend fun getNextInvoice(token: String): NextInvoice

    suspend fun getWorkoutsForMember(token: String): MemberWorkouts

    suspend fun getLocations(): List<Location>
    suspend fun getLocationStatistics(id: String): LocationStatistics?
    suspend fun getLocationStatisticsTimeline(id: String): LocationStatisticsTimeline?
}

