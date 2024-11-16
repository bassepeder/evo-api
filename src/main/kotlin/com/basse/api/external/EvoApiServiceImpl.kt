package com.basse.api.external

import com.basse.Constants
import com.basse.api.external.responses.EvoAuthenticateUserResponse
import com.basse.api.responses.CurrentPaymentMethod
import com.basse.api.responses.Invoice
import com.basse.api.responses.LocationDetails
import com.basse.api.responses.MemberWorkouts
import com.basse.api.responses.MembershipDetails
import com.basse.api.responses.MembershipDetailsResponse
import com.basse.api.responses.MembershipFreeze
import com.basse.api.responses.MembershipStatus
import com.basse.api.responses.NextInvoice
import com.basse.api.responses.ProductDetails
import com.basse.api.responses.WorkoutMonth

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
                    locale = details.locale,
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
}