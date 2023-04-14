package sinkoin.models

import kotlinx.serialization.Serializable
import sinkoin.serializers.UUIDSerializer
import java.util.*

@Serializable
data class Persona(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID = UUID.randomUUID(),
    val nombre: String = listOf("Pepe", "Juan", "Luis", "Ana", "Maria", "Lola").random(),
    val edad: Int = (18..50).random(),
)