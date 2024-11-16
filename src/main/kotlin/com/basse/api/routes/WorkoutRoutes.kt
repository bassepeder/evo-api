package com.basse.api.routes

import com.basse.api.external.EvoApiService
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route

fun Route.workoutRoutes(service: EvoApiService) {
    route("api/v1/workouts") {
        get {
            val token = call.request.headers[HttpHeaders.Authorization]

            val result = service.getWorkoutsForMember(token!!)

            call.respond(HttpStatusCode.OK, result)
        }
    }
}
