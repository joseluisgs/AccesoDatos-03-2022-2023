package validators

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import errors.PersonasError
import models.Persona


fun Persona.validarNombreResultError(): Result<Persona, PersonasError> {
    if (nombre.isBlank()) {
        return Err(PersonasError.PersonaDatosNoValidosError("El nombre no puede estar vacío"))
    }
    return Ok(this)
}

fun Persona.validarEdadResultError(): Result<Persona, PersonasError> {
    if (edad < 0) {
        return Err(PersonasError.PersonaDatosNoValidosError("La edad no puede ser negativa"))
    }
    return Ok(this)
}
