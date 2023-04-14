package controllers

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.andThen
import errors.PersonasError
import models.Persona
import respositories.PersonasRepository
import validators.validarEdadResultError
import validators.validarNombreResultError

class PersonasResultErrorController(
    private val personasRepository: PersonasRepository
) {

    fun getAll() = personasRepository.findAll()

    fun getById(id: Int): Result<Persona, PersonasError> {
        personasRepository.findById(id)?.let {
            return Ok(it)
        } ?: return Err(PersonasError.PersonaNoEncontradaError(id))
    }

    fun create(persona: Persona): Result<Persona, PersonasError> {
        return persona.validarNombreResultError()
            .andThen { it.validarEdadResultError() }
            .andThen { Ok(personasRepository.save(persona)) }
    }

    fun delete(id: Int): Result<Boolean, PersonasError> {
        if (!personasRepository.delete(id)) {
            return Err(PersonasError.PersonaNoEncontradaError(id))
        }
        return Ok(true)
    }
}