package com.basse.api.responses.location

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class Location(
    val id: String,
    val name: String,
    val street: String,
    val openingDate: LocalDate,
    val closingDate: LocalDate?,
)
