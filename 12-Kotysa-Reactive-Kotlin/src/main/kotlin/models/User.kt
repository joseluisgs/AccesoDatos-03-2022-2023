package models

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import serializers.UUIDSerializer
import java.util.*

/**
 * Al usar Kotlin Data Time ya no necesito usar la clase java.time.LocalDateTime
 * ni serializarla con el serializer LocalDateTimeSerializer
 * https://github.com/Kotlin/kotlinx-datetime
 */

@Serializable
data class User(
    val firstname: String,
    val lastname: String,
    val isAdmin: Boolean,
    @Serializable(with = UUIDSerializer::class)
    val roleId: UUID,
    val alias: String? = null,
    val creationTime: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
    val id: Int? = null
)
