package koin.services

import koin.models.Persona
import kotlinx.serialization.StringFormat

/**
 * Interfaz para parsear personas [Persona] a [String] y viceversa con formato [StringFormat]
 */
interface PersonasParser : Parser<Persona, StringFormat> {
    override fun encodeToString(value: List<Persona>): String
    override fun decodeFromString(string: String): List<Persona>
}