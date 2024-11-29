package com.basse.api.external

import com.basse.Constants
import com.basse.api.external.responses.EvoAuthenticateUserResponse
import com.basse.api.responses.CurrentPaymentMethod
import com.basse.api.responses.Invoice
import com.basse.api.responses.location.Location
import com.basse.api.responses.LocationDetails
import com.basse.api.responses.location.LocationStatistics
import com.basse.api.responses.MemberWorkouts
import com.basse.api.responses.MembershipDetails
import com.basse.api.responses.MembershipDetailsResponse
import com.basse.api.responses.MembershipFreeze
import com.basse.api.responses.MembershipStatus
import com.basse.api.responses.NextInvoice
import com.basse.api.responses.ProductDetails
import com.basse.api.responses.WorkoutMonth
import com.basse.api.responses.location.LocationStatisticsTimeline
import com.basse.api.responses.location.LocationStatisticsTimelineInterval
import kotlin.String

class EvoApiServiceImpl(private val apiClient: EvoApiClient): EvoApiService {

    override suspend fun authenticateUser(username: String, password: String): Result<EvoAuthenticateUserResponse> =
        apiClient.authenticateUser(
            operatorId = Constants.OPERATOR_ID,
            username = username,
            password = password
        )

    override suspend fun getMembershipDetails(token: String): MembershipDetailsResponse {
        val details = apiClient.getMembershipDetails(token)
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

    override suspend fun getInvoices(token: String): List<Invoice> {
        var invoices = apiClient.getInvoices(token)

        return invoices.map { invoice -> Invoice(
            id = invoice.id,
            invoiceNumber = invoice.invoiceNumber,
            date = invoice.invoiceDate,
            amount = invoice.amount,
            currency = invoice.currency,
            status = invoice.status,
            from = invoice.period.from,
            to = invoice.period.to,
        ) }
    }

    override suspend fun getNextInvoice(token: String): NextInvoice {
        val invoice = apiClient.getNextInvoice(token)

        return NextInvoice(
            date = invoice.invoiceDate,
            amount = invoice.amount,
        )
    }

    override suspend fun getWorkoutsForMember(token: String): MemberWorkouts {
        val workouts = apiClient.getWorkoutsForMember(token)

        return MemberWorkouts(
            totalWorkouts = workouts.totalWorkouts,
            workoutMonths = workouts.workoutMonths.map { month -> WorkoutMonth(
                totalWorkouts = month.totalWorkouts,
                year = month.year,
                month = month.month,
            )}
        )
    }

    override suspend fun getLocations(): List<Location> {
        val locations = apiClient.getLocations()

        return locations.map { l -> Location(id = l.id, name = l.name ) }
    }

    override suspend fun getLocationStatistics(id: String): LocationStatistics? {
        val statistics = apiClient.getLocationStatistics(id)

        return statistics?.let { s -> LocationStatistics(
            id = s.id,
            name = s.name,
            current = s.current,
            maxCapacity = s.maxCapacity,
            percentageUsed = s.percentageUsed,
        ) }
    }

    override suspend fun getLocationStatisticsTimeline(id: String): LocationStatisticsTimeline? {
        val timeline = apiClient.getLocationStatisticsTimeline(id)

        return timeline?.let { t -> LocationStatisticsTimeline(
            id = t.id,
            name = t.name,
            intervals = t.intervals.map { i -> LocationStatisticsTimelineInterval(
                name = i.name,
                begin = i.begin,
                end = i.end,
                maxCapacity = i.maxCapacity,
                onsiteMinutes = (i.onsiteMinutes as? Int) ?: (i.onsiteMinutes.toString().toIntOrNull() ?: 0), // Shit design by EVO.
                percentageUsed = i.percentageUsed,
                status = i.status,
            )},
        ) }
    }
}