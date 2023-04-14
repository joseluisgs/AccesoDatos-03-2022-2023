package errors

// Si le ponemos las llaves podemos usarlos como un enum
sealed class PersonasError(val mensage: String) {
    class PersonaNoEncontradaError(id: Int) : PersonasError("ERROR: No se encontró la persona con id $id")
    class PersonaDatosNoValidosError(mensage: String) : PersonasError("ERROR: $mensage")
}