package sinkoin.services

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import mu.KotlinLogging
import nl.adaptivity.xmlutil.serialization.XML
import sinkoin.models.Persona

/**
 * Clase para parsear personas [Persona] a [String] y viceversa con formato [XML]
 */

private val logger = KotlinLogging.logger {}

class PersonasParserXml(private val parser: XML = XML { indent = 4 }) : PersonasParser {

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