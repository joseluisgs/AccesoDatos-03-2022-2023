package koin.services

import koin.models.Persona
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import mu.KotlinLogging
import nl.adaptivity.xmlutil.serialization.XML
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

/**
 * Clase para parsear personas [Persona] a [String] y viceversa con formato [XML]
 */

private val logger = KotlinLogging.logger {}

@Single
@Named("PersonasParserXml")
class PersonasParserXml : PersonasParser {
    private val parser: XML = XML { indent = 4 }

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