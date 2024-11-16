package com.basse.api.responses

import kotlinx.serialization.Serializable

@Serializable
data class NextInvoice(
    val date: String,
    val amount: Double,
)
