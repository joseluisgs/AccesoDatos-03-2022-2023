package koin.controllers


import koin.models.Persona
import koin.repositories.PersonasRepository
import koin.services.PersonasParser
import mu.KotlinLogging
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single
import java.util.*

private val logger = KotlinLogging.logger {}

@Single
// @Named("PersonasControllerJson")
class PersonasController(
    @Named("PersonasRepository") private val personasRepository: PersonasRepository,
    @Named("PersonasParserJson") private val personasParser: PersonasParser
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