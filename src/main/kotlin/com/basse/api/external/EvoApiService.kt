package com.basse.api.external

import com.basse.api.external.responses.EvoAuthenticateUserResponse
import com.basse.api.requests.UpdatePrimaryLocationRequest
import com.basse.api.requests.UpdateProfileGdprConsentRequest
import com.basse.api.requests.UpdateProfileRequest
import com.basse.api.responses.*
import com.basse.api.responses.location.Location
import com.basse.api.responses.location.LocationStatistics
import com.basse.api.responses.location.LocationStatisticsTimeline
import kotlinx.datetime.LocalDate

interface EvoApiService {
    suspend fun authenticateUser(
        username: String,
        password: String,
    ): Result<EvoAuthenticateUserResponse>

    suspend fun getMembershipDetails(token: String): Result<MembershipDetailsResponse>
    suspend fun getCurrentMembershipReferral(token: String): CurrentMembershipReferral

    suspend fun updatePrimaryLocation(token: String, request: UpdatePrimaryLocationRequest): Boolean
    suspend fun updateProfile(token: String, request: UpdateProfileRequest): Boolean
    suspend fun updateProfileGdprConsent(token: String, request: UpdateProfileGdprConsentRequest): Boolean

    suspend fun getInvoices(token: String): List<Invoice>
    suspend fun getNextInvoice(token: String): NextInvoice

    suspend fun getWorkoutsForMember(token: String): MemberWorkouts

    suspend fun getLocations(token: String): List<Location>
    suspend fun getLocationStatistics(id: String): LocationStatistics?
    suspend fun getLocationStatisticsTimeline(id: String): LocationStatisticsTimeline?
    suspend fun getLocationStatisticsTimelineForDate(id: String, date: LocalDate): LocationStatisticsTimeline?
}

