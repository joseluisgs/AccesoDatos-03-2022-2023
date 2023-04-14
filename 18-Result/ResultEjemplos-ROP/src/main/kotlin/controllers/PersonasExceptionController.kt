package controllers

import exceptions.PersonaNoEncontradaException
import models.Persona
import respositories.PersonasRepository
import validators.validarEdadException
import validators.validarNombreException

class PersonasExceptionController(
    private val personasRepository: PersonasRepository
) {

    fun getAll() = personasRepository.findAll()

    fun getById(id: Int): Persona {
        return personasRepository.findById(id) ?: throw PersonaNoEncontradaException(id)
    }

    fun create(persona: Persona): Persona {
        persona.validarNombreException() // Lanza una excepción si los datos no son válidos
        persona.validarEdadException()
        return personasRepository.save(persona)
    }

    fun delete(id: Int): Boolean {
        if (!personasRepository.delete(id)) {
            throw PersonaNoEncontradaException(id)
        }
        return true
    }
}