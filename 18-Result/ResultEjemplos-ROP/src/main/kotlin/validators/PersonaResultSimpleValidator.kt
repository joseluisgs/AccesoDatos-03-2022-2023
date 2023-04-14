package validators

import io.getstream.result.Error
import io.getstream.result.Result
import models.Persona


fun Persona.validarNombreResultSimple(): Result<Persona> {
    if (nombre.isBlank()) {
        return Result.Failure(value = Error.GenericError("ERROR: El nombre no puede estar vacío"))
    }
    return Result.Success(value = this)
}

fun Persona.validarEdadResultSimple(): Result<Persona> {
    if (edad < 0) {
        return Result.Failure(value = Error.GenericError("ERROR: La edad no puede ser negativa"))
    }
    return Result.Success(value = this)
}
