package com.basse.api.routes

import com.basse.api.external.EvoApiService
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route

fun Route.invoiceRoutes(service: EvoApiService) {
    route("api/v1/invoices") {
        get {
            val token = call.request.headers[HttpHeaders.Authorization]

            val result = service.getInvoices(token!!)

            call.respond(HttpStatusCode.OK, result)
        }

        get("next") {
            val token = call.request.headers[HttpHeaders.Authorization]

            val result = service.getNextInvoice(token!!)

            call.respond(HttpStatusCode.OK, result)
        }
    }
}
