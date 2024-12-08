package com.basse.api.external

import com.basse.Constants
import com.basse.api.external.requests.EvoAuthenticateUserRequest
import com.basse.api.external.responses.*
import com.basse.api.external.responses.invoices.EvoInvoicesResponse
import com.basse.api.external.responses.invoices.EvoNextInvoiceResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.datetime.LocalDate
import kotlinx.serialization.json.Json

private const val INCORRECT_PASSWORD_API_REASON = "Error: Password check failed"

class EvoApiClient  {
    private val defaultClient: HttpClient by lazy {
        HttpClient(CIO) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
            install(DefaultRequest) {
                url {
                    protocol = URLProtocol.HTTPS
                    host = Constants.API_BASE_HOST
                }
                header(HttpHeaders.Referrer, Constants.CLIENT_REFERRER)
                header(HttpHeaders.Origin, Constants.CLIENT_ORIGIN)
                contentType(ContentType.Application.Json)
            }
        }
    }

    private val visitsClient: HttpClient by lazy {
        HttpClient(CIO) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
            install(DefaultRequest) {
                url {
                    protocol = URLProtocol.HTTPS
                    host = Constants.API_BASE_HOST_VISITS
                }
                header(HttpHeaders.Referrer, Constants.CLIENT_REFERRER)
                header(HttpHeaders.Origin, Constants.CLIENT_ORIGIN)
                contentType(ContentType.Application.Json)
            }
        }
    }

    class UnauthorizedException : Exception()
    class UserNotFoundException : Exception()

    suspend fun authenticateUser(
        operatorId: String,
        username: String,
        password: String
    ): Result<EvoAuthenticateUserResponse> {
        val request = EvoAuthenticateUserRequest(operatorId, username, password)

        return try {
            val response: HttpResponse = defaultClient.post("auth/authenticate") {
                setBody(request)
            }

            if (response.status.isSuccess()) {
                val responseBody = response.body<EvoAuthenticateUserResponse>()
                Result.success(responseBody)
            } else {
                if (response.status == HttpStatusCode.NotFound) {
                    return Result.failure(UserNotFoundException())
                }

                val responseBody = response.body<EvoApiErrorResponse>()
                if (responseBody.reason == INCORRECT_PASSWORD_API_REASON) {
                    return Result.failure(UnauthorizedException())
                }

                return Result.failure(Exception("HTTP error: ${response.status.value}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getMembershipDetails(token: String): Result<EvoMembershipDetailsResponse> {
        return try {
            val response: HttpResponse = defaultClient.get("v2/membership") {
                header(HttpHeaders.Authorization, token)
            }

            if (response.status.isSuccess()) {
                val body = response.body<EvoMembershipDetailsResponse>()
                Result.success(body)
            } else {
                val responseBody = response.body<EvoExpiredTokenResponse>()
                if (responseBody.errorCode == 401) {
                    return Result.failure(UnauthorizedException())
                }

                return Result.failure(Exception("HTTP error: ${response.status.value}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getInvoices(token: String): List<EvoInvoicesResponse> {
        val response: HttpResponse = defaultClient.get("v1/membership/invoices") {
            header(HttpHeaders.Authorization, token)
        }

        return response.body<List<EvoInvoicesResponse>>()
    }

    suspend fun getNextInvoice(token: String): EvoNextInvoiceResponse {
        val response: HttpResponse = defaultClient.get("v1/membership/invoices/next") {
            header(HttpHeaders.Authorization, token)
        }

        return response.body<EvoNextInvoiceResponse>()
    }

    suspend fun getWorkoutsForMember(token: String): EvoMemberWorkoutsResponse {
        val response: HttpResponse = defaultClient.get("v2/workouts") {
            header(HttpHeaders.Authorization, token)
        }

        return response.body<EvoMemberWorkoutsResponse>()
    }

    suspend fun getLocations(): List<EvoLocationDetails> {
        val response: HttpResponse = visitsClient.get("api/v1/locations?operator=${Constants.OPERATOR_ID}")

        return response.body<List<EvoLocationDetails>>()
    }

    suspend fun getLocationStatistics(id: String): EvoLocationStatistics? {
        val response: HttpResponse = visitsClient.get("api/v1/locations/${id}/current")

        return when {
            response.status == HttpStatusCode.InternalServerError -> null
            response.status == HttpStatusCode.NotFound -> null
            response.status.isSuccess() -> response.body<EvoLocationStatistics>()
            else -> null
        }
    }

    suspend fun getLocationStatisticsTimeline(id: String): EvoLocationStatisticsTimeline? {
        val response: HttpResponse = visitsClient.get("api/v1/locations/${id}/timeline")

        return when {
            response.status == HttpStatusCode.InternalServerError -> null
            response.status == HttpStatusCode.NotFound -> null
            response.status.isSuccess() -> response.body<EvoLocationStatisticsTimeline>()
            else -> null
        }
    }

    suspend fun getLocationStatisticsTimelineForDate(id: String, date: LocalDate): EvoLocationStatisticsTimeline? {
        val response: HttpResponse = visitsClient.get("api/v1/locations/${id}/timeline?date=$date")

        return when {
            response.status == HttpStatusCode.InternalServerError -> null
            response.status == HttpStatusCode.NotFound -> null
            response.status.isSuccess() -> response.body<EvoLocationStatisticsTimeline>()
            else -> null
        }
    }
}
