package com.basse.api.external.responses.invoices

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EvoInvoicesResponse(
    @SerialName("id") val id: String,
    @SerialName("invoice_number") val invoiceNumber: Long,
    @SerialName("invoice_date") val invoiceDate: String,
    @SerialName("amount") val amount: Double,
    @SerialName("currency") val currency: String,
    @SerialName("status") val status: String,
    @SerialName("period") val period: EvoInvoicePeriod,
)

@Serializable
data class EvoInvoicePeriod(
    @SerialName("from") val from: String,
    @SerialName("to") val to: String,
)
