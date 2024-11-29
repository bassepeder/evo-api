package com.basse

import com.basse.api.external.EvoApiClient
import com.basse.api.external.EvoApiServiceImpl
import com.basse.api.routes.authRoutes
import com.basse.api.routes.invoiceRoutes
import com.basse.api.routes.locationRoutes
import com.basse.api.routes.membershipRoutes
import com.basse.api.routes.workoutRoutes
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

@OptIn(ExperimentalSerializationApi::class)
fun Application.module() {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            explicitNulls = false
            namingStrategy = JsonNamingStrategy.SnakeCase
        })
    }

    val evoApiService = EvoApiServiceImpl(EvoApiClient())

    routing {
        authRoutes(evoApiService)
        membershipRoutes(evoApiService)
        invoiceRoutes(evoApiService)
        workoutRoutes(evoApiService)
        locationRoutes(evoApiService)
    }
}
