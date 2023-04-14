package koin.repositories

import koin.models.Persona
import java.util.*

/**
 * Repository for personas [Persona] con ID [UUID]
 */
interface PersonasRepository : CrudRepository<Persona, UUID> {
    fun findByNombre(nombre: String): List<Persona>
}