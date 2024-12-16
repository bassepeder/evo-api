package com.basse.api.routes

import com.basse.api.external.EvoApiClient.UnauthorizedException
import com.basse.api.external.EvoApiService
import com.basse.api.requests.UpdatePrimaryLocationRequest
import com.basse.api.requests.UpdateProfileRequest
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.membershipRoutes(service: EvoApiService) {
    route("api/v1/membership") {
        get {
            val token = call.request.headers[HttpHeaders.Authorization]!!

            val result = service.getMembershipDetails(token)

            result.fold(
                onSuccess = { response ->
                    call.respond(HttpStatusCode.OK, response)
                },
                onFailure = { exception ->
                    if (exception is UnauthorizedException)
                        call.respond(HttpStatusCode.Unauthorized)
                    else
                        call.respond(HttpStatusCode.InternalServerError)
                }
            )
        }

        put("update-location") {
            val token = call.request.headers[HttpHeaders.Authorization]!!
            val request = call.receive<UpdatePrimaryLocationRequest>()

            val ok = service.updatePrimaryLocation(token, request)

            if (ok)
                call.respond(HttpStatusCode.OK)
            else
                call.respond(HttpStatusCode.InternalServerError)
        }

        patch("profile") {
            val token = call.request.headers[HttpHeaders.Authorization]!!
            val request = call.receive<UpdateProfileRequest>()

            if (!request.isValid()) {
                call.respond(HttpStatusCode.BadRequest)
                return@patch
            }

            val ok = service.updateProfile(token, request)

            if (ok)
                call.respond(HttpStatusCode.OK)
            else
                call.respond(HttpStatusCode.InternalServerError)
        }
    }
}
