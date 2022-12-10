package sinkoin.services

import kotlinx.serialization.StringFormat
import sinkoin.models.Persona

/**
 * Interfaz para parsear personas [Persona] a [String] y viceversa con formato [StringFormat]
 */
interface PersonasParser : Parser<Persona, StringFormat> {
    override fun encodeToString(value: List<Persona>): String
    override fun decodeFromString(string: String): List<Persona>
}