package controllers

import io.getstream.result.Error
import io.getstream.result.Result
import io.getstream.result.then
import models.Persona
import respositories.PersonasRepository
import validators.validarEdadResultSimple
import validators.validarNombreResultSimple

class PersonasResultSimpleController(
    private val personasRepository: PersonasRepository
) {

    fun getAll() = personasRepository.findAll()

    fun getById(id: Int): Result<Persona> {
        personasRepository.findById(id)?.let {
            return Result.Success(it)
        } ?: return Result.Failure(value = Error.GenericError("ERROR: No se ha encontrado la persona con id $id"))
    }

    fun create(persona: Persona): Result<Persona> {
        return persona.validarNombreResultSimple()
            .then { it.validarEdadResultSimple() }
            .onSuccess {
                Result.Success(value = personasRepository.save(persona))
            }
    }

    fun delete(id: Int): Result<Boolean> {
        if (!personasRepository.delete(id)) {
            Result.Failure(value = Error.GenericError("No se ha encontrado la persona con id $id"))
        }
        return Result.Success(value = true)
    }
}