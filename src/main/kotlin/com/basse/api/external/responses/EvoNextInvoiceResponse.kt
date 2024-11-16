package com.basse.api.external.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EvoNextInvoiceResponse(
    @SerialName("invoice_date") val invoiceDate: String,
    @SerialName("amount") val amount: Double,
)
