package respositories

import models.Persona

object PersonasRepository {
    val personas = mutableListOf(
        Persona(1, "Juan", 20),
        Persona(2, "Ana", 30),
        Persona(3, "Pedro", 40)
    )

    fun findAll() = personas

    fun findById(id: Int) = personas.find { it.id == id }

    fun save(persona: Persona): Persona {
        val lastId = personas.maxByOrNull { it.id }?.id ?: 0
        val newPersona = persona.copy(id = lastId + 1)
        personas.add(newPersona)
        return newPersona
    }

    fun delete(id: Int) = personas.removeIf { it.id == id }
}