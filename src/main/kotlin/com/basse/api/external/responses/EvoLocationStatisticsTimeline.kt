package com.basse.api.external.responses

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*

@Serializable
data class EvoLocationStatisticsTimeline(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String,
    @SerialName("intervals") val intervals: List<EvoLocationStatisticsTimelineInterval>,
)

@Serializable
data class EvoLocationStatisticsTimelineInterval(
    @SerialName("name") val name: String,
    @SerialName("begin") val begin: Int,
    @SerialName("end") val end: Int,
    @SerialName("maxCapacity") val maxCapacity: Int,
    @SerialName("onsiteMinutes") @Serializable(with = OnsiteMinutesSerializer::class) val onsiteMinutes: Any,
    @SerialName("percentageUsed") val percentageUsed: Double,
    @SerialName("status") val status: String,
)

object OnsiteMinutesSerializer : KSerializer<Any> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("OnsiteMinutes", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Any) {
        when (value) {
            is String -> encoder.encodeString(value)
            is Int -> encoder.encodeInt(value)
            else -> throw IllegalArgumentException("Unsupported type for onsiteMinutes: ${value::class}")
        }
    }

    override fun deserialize(decoder: Decoder): Any {
        return try {
            decoder.decodeString()
        } catch (e: Exception) {
            decoder.decodeInt()
        }
    }
}
