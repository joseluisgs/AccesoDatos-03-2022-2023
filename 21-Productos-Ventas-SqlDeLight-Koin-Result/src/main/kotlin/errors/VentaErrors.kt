package errors

// Errores de dominio
sealed class VentaError(val message: String) {
    class VentaNoExisteError(message: String) : VentaError("ERROR: Carrito no encontrado: $message")
    class VentaNoGuardadoError(message: String) : VentaError("ERROR: Carrito no guardado: $message")
    class VentaNoValidoError(message: String) : VentaError("ERROR: Carrito no valido: $message")
}
