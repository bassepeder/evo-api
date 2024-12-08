package com.basse.api.routes

import com.basse.api.external.EvoApiClient.UnauthorizedException
import com.basse.api.external.EvoApiClient.UserNotFoundException
import com.basse.api.external.EvoApiService
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.membershipRoutes(service: EvoApiService) {
    route("api/v1/membership") {
        get {
            val token = call.request.headers[HttpHeaders.Authorization]

            val result = service.getMembershipDetails(token!!)

            result.fold(
                onSuccess = { response ->
                    call.respond(HttpStatusCode.OK, response)
                },
                onFailure = { exception ->
                    if (exception is UnauthorizedException || exception is UserNotFoundException)
                        call.respond(HttpStatusCode.Unauthorized)
                    else
                        call.respond(HttpStatusCode.InternalServerError)
                }
            )
        }
    }
}
