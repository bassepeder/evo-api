package com.basse.api.external.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EvoCurrentMembershipReferral(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String,
    @SerialName("referrer_discount_months") val discountMonthsCount: Int,
    @SerialName("referrer_discount_percentage") val discountPercentage: Int,
    @SerialName("referrer_pt_hours") val includedPtHoursAmount: Int,
    @SerialName("referrer_presentation") val description: String,
    @SerialName("sales_period") val salesPeriod: EvoCurrentMembershipReferralPeriod,
)

@Serializable
data class EvoCurrentMembershipReferralPeriod(
    @SerialName("from") val from: String,
    @SerialName("to") val to: String?,
)
