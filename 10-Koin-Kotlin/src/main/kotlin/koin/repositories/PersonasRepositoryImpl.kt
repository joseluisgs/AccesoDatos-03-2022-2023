package koin.repositories

import koin.models.Persona
import mu.KotlinLogging
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single
import java.util.*

/**
 * Repository Implementation for personas [Persona] con ID [UUID]
 */

private val logger = KotlinLogging.logger {}

@Single
@Named("PersonasRepository")
class PersonasRepositoryImpl : PersonasRepository {
    // Para hacerlo más sencillo, vamos a usar un mapa
    // en lugar de una base de datos
    private val personas = mutableMapOf<UUID, Persona>()

    override fun findByNombre(nombre: String): List<Persona> {
        logger.debug { "findByNombre: $nombre" }
        return personas.values.filter { it.nombre == nombre }
    }

    override fun findAll(): List<Persona> {
        logger.debug { "findAll" }
        return personas.values.toList()
    }

    override fun findById(id: UUID): Persona? {
        logger.debug { "findById: $id" }
        return personas[id]
    }

    override fun save(item: Persona): Persona {
        logger.debug { "save: $item" }
        personas[item.id] = item
        return item
    }

    override fun delete(item: Persona): Persona {
        logger.debug { "delete: $item" }
        personas.remove(item.id)
        return item
    }

    override fun deleteById(id: UUID): Persona? {
        logger.debug { "deleteById: $id" }
        return personas.remove(id)
    }

    override fun deleteAll(): Boolean {
        logger.debug { "deleteAll" }
        personas.clear()
        return personas.isEmpty()
    }
}