package com.basse.api.external

import com.basse.Constants
import com.basse.api.external.EvoApiClient.UnauthorizedException
import com.basse.api.external.EvoApiClient.UserNotFoundException
import com.basse.api.external.responses.EvoAuthenticateUserResponse
import com.basse.api.requests.UpdateProfileRequest
import com.basse.api.responses.*
import com.basse.api.responses.location.Location
import com.basse.api.responses.location.LocationStatistics
import com.basse.api.responses.location.LocationStatisticsTimeline
import kotlinx.datetime.LocalDate

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
                return Result.success(MembershipDetailsResponse.mapFromEvoResponse(details))
            },
            onFailure = { exception ->
                if (exception is UnauthorizedException || exception is UserNotFoundException)
                    return Result.failure(exception)
                else
                    throw Exception("Unknown HTTP error")
            }
        )
    }

    override suspend fun updateProfile(token: String, request: UpdateProfileRequest): Boolean {
        return apiClient.updateProfile(token, request.toExternalRequest())
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

    override suspend fun getLocations(token: String): List<Location> {
        val locations = apiClient.getLocations(token)

        return locations.map { l ->
            Location(
                id = l.id,
                name = l.name,
                street = l.street!!,
                openingDate = LocalDate.parse(l.openingDate!!),
                closingDate = if (l.closingDate == null) null else LocalDate.parse(l.closingDate)
            )
        }
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

        return timeline?.let { t -> LocationStatisticsTimeline.fromExternalResponse(t) }
    }

    override suspend fun getLocationStatisticsTimelineForDate(
        id: String,
        date: LocalDate
    ): LocationStatisticsTimeline? {
        val timeline = apiClient.getLocationStatisticsTimelineForDate(id, date)

        return timeline?.let { t -> LocationStatisticsTimeline.fromExternalResponse(t) }
    }
}