package com.basse

import com.basse.api.external.EvoApiClient
import com.basse.api.external.EvoApiServiceImpl
import com.basse.api.routes.authRoutes
import com.basse.api.routes.membershipRoutes
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.uri
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNamingStrategy

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.callAuthorizationMiddleware() {
    intercept(ApplicationCallPipeline.ApplicationPhase.Plugins) {
        val authHeader = call.request.headers[HttpHeaders.Authorization]
        if (call.request.uri != "/api/v1/auth/authenticate" && authHeader.isNullOrBlank()) {
            call.respond(HttpStatusCode.Unauthorized, "Authorization header is missing.")
            finish()
        }
    }
}

@OptIn(ExperimentalSerializationApi::class)
fun Application.module() {
    callAuthorizationMiddleware()
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            explicitNulls = false
            namingStrategy = JsonNamingStrategy.SnakeCase
        })
    }
    routing {
        authRoutes(EvoApiServiceImpl(EvoApiClient()))
        membershipRoutes(EvoApiServiceImpl(EvoApiClient()))
    }
}
