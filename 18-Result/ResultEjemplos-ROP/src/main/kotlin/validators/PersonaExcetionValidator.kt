package validators

import exceptions.PersonaDatosNoValidosException
import models.Persona


fun Persona.validarNombreException() {
    if (nombre.isBlank()) {
        throw PersonaDatosNoValidosException("El nombre no puede estar vacío")
    }
}

fun Persona.validarEdadException() {
    if (edad < 0) {
        throw PersonaDatosNoValidosException("La edad no puede ser negativa")
    }
}