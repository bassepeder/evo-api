package com.basse.api.responses

import kotlinx.serialization.Serializable

@Serializable
data class Invoice(
    val id: String,
    val invoiceNumber: Long,
    val date: String,
    val amount: Double,
    val currency: String,
    val status: String,
    val from: String,
    val to: String
)
