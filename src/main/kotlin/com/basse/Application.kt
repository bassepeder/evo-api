package com.basse

import com.basse.api.external.EvoApiClient
import com.basse.api.external.EvoApiServiceImpl
import com.basse.api.routes.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
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
        ignoreType<HttpStatusCode>()
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
