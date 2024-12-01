package com.basse.api.routes

import com.basse.api.external.EvoApiService
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import kotlinx.datetime.LocalDate

fun Route.locationRoutes(service: EvoApiService) {
    route("api/v1/locations") {
        get {
            val result = service.getLocations()

            call.respond(HttpStatusCode.OK, result)
        }

        get("/{id}") {
            val id = call.parameters["id"] ?: return@get call.respond(
                HttpStatusCode.BadRequest,
                "The location 'id' is either missing or invalid."
            )

            val result = service.getLocationStatistics(id) ?: return@get call.respond(
                HttpStatusCode.NotFound,
                "Location with id '$id' was not found."
            )

            call.respond(HttpStatusCode.OK, result)
        }

        get("/{id}/timeline/current") {
            val id = call.parameters["id"] ?: return@get call.respond(
                HttpStatusCode.BadRequest,
                "The location 'id' is either missing or invalid."
            )

            val result = service.getLocationStatisticsTimeline(id) ?: return@get call.respond(
                HttpStatusCode.NotFound,
                "Location with id '$id' was not found."
            )

            call.respond(HttpStatusCode.OK, result)
        }

        get("/{id}/timeline") {
            val id = call.parameters["id"] ?: return@get call.respond(
                HttpStatusCode.BadRequest,
                "The location 'id' is either missing or invalid."
            )

            val date = call.queryParameters["date"] ?: return@get call.respond(
                HttpStatusCode.BadRequest,
                "The required 'date' query parameter is missing."
            )

            val parsedDate = try {
                LocalDate.parse(date)
            } catch (_: Exception) {
                return@get call.respond(
                    HttpStatusCode.BadRequest,
                    "The provided 'date' query parameter is not in the expected format (yyyy-MM-dd)."
                )
            }

            val result = service.getLocationStatisticsTimelineForDate(id, parsedDate) ?: return@get call.respond(
                HttpStatusCode.NotFound,
                "Location with id '$id' was not found."
            )

            call.respond(HttpStatusCode.OK, result)
        }
    }
}
