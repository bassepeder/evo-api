package com.basse.api.responses

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class CurrentMembershipReferral(
    val id: String,
    val name: String,
    val discountMountsCount: Int,
    val discountPercentage: Int,
    val includedPtHoursAmount: Int,
    val description: String,
    val validFrom: LocalDateTime,
    val validUntil: LocalDateTime?
)
