package sinkoin.services

import kotlinx.serialization.StringFormat
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import mu.KotlinLogging
import sinkoin.models.Persona

/**
 * Clase para parsear personas [Persona] a [String] y viceversa con formato [StringFormat]
 */

private val logger = KotlinLogging.logger {}

class PersonasParserImpl(private val parser: StringFormat = Json { prettyPrint = true }) : PersonasParser {

    override fun encodeToString(value: List<Persona>): String {
        logger.debug { "encodeToString: $value" }
        return parser.encodeToString(value)
    }

    override fun encodeToString(value: Persona): String {
        logger.debug { "encodeToString: $value" }
        return parser.encodeToString(value)
    }

    override fun decodeFromString(string: String): List<Persona> {
        logger.debug { "decodeFromString: $string" }
        return parser.decodeFromString(string)
    }
}