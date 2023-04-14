package sinkoin.controllers

import mu.KotlinLogging
import sinkoin.models.Persona
import sinkoin.repositories.PersonasRepository
import sinkoin.services.PersonasParser
import java.util.*

private val logger = KotlinLogging.logger {}

class PersonasController(
    private val personasRepository: PersonasRepository,
    private val personasParser: PersonasParser
) {

    fun getPersonas(): String {
        logger.debug { "getPersonas" }
        val personas = personasRepository.findAll()
        return personasParser.encodeToString(personas)
    }

    fun getPersonaById(id: UUID): String {
        logger.debug { "getPersonaById: $id" }
        val persona = personasRepository.findById(id) ?: throw Exception("Persona  con id $id no encontrada")
        return personasParser.encodeToString(persona)
    }

    fun getPersonaByNombre(nombre: String): String {
        logger.debug { "getPersonaByNombre: $nombre" }
        val personas =
            personasRepository.findByNombre(nombre)
        return personasParser.encodeToString(personas)
    }

    fun createPersona(persona: Persona): String {
        logger.debug { "createPersona: $persona" }
        val p = personasRepository.save(persona)
        return personasParser.encodeToString(p)
    }

    fun updatePersona(persona: Persona): String {
        logger.debug { "updatePersona: $persona" }
        val p = personasRepository.save(persona)
        return personasParser.encodeToString(p)
    }

    fun deletePersona(persona: Persona): String {
        logger.debug { "deletePersona: $persona" }
        val p = personasRepository.delete(persona)
        return personasParser.encodeToString(p)
    }

    fun deletePersonaById(id: UUID): String {
        logger.debug { "deletePersonaById: $id" }
        var p = personasRepository.findById(id) ?: throw Exception("Persona  con id $id no encontrada")
        p = personasRepository.delete(p)
        return personasParser.encodeToString(p)
    }

}