package com.basse.api.external

import com.basse.Constants
import com.basse.api.external.requests.EvoAuthenticateUserRequest
import com.basse.api.external.responses.EvoApiErrorResponse
import com.basse.api.external.responses.EvoAuthenticateUserResponse
import com.basse.api.external.responses.EvoMembershipDetailsResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

private const val INCORRECT_PASSWORD_API_REASON = "Error: Password check failed"

class EvoApiClient  {
    private val client: HttpClient by lazy {
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

    class UnauthorizedException : Exception()
    class UserNotFoundException : Exception()

    suspend fun authenticateUser(
        operatorId: String,
        username: String,
        password: String
    ): Result<EvoAuthenticateUserResponse> {
        val request = EvoAuthenticateUserRequest(operatorId, username, password)

        return try {
            val response: HttpResponse = client.post("auth/authenticate") {
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
        val response: HttpResponse = client.get("v2/membership") {
            header(HttpHeaders.Authorization, token)
        }

        val responseBody = response.body<EvoMembershipDetailsResponse>()

        return Result.success(responseBody)
    }
}
