package exceptions

sealed class PersonasException(val mensage: String) : Exception(mensage)
class PersonaNoEncontradaException(id: Int) : PersonasException("ERROR: No se encontró la persona con id $id")
class PersonaDatosNoValidosException(mensage: String) : PersonasException("ERROR: $mensage")
