package com.basse.api.routes

import com.basse.api.external.EvoApiClient.UnauthorizedException
import com.basse.api.external.EvoApiClient.UserNotFoundException
import com.basse.api.external.EvoApiService
import com.basse.api.requests.AuthenticateUserRequest
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.authRoutes(service: EvoApiService) {
    route("/api/v1/auth") {
        post("/authenticate") {
            val request = call.receive<AuthenticateUserRequest>()

            if (request.username.isBlank() || request.password.isBlank()) {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }

            val result = service.authenticateUser(request.username, request.password)

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