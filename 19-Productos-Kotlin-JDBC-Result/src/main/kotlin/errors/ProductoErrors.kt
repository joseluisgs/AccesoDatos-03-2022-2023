package errors

sealed class ProductoError(val message: String) {
    class NoEncontrado(message: String) : ProductoError("ERROR: Producto no encontrado: $message")
    class NoGuardado(message: String) : ProductoError("ERROR: Producto no guardado: $message")
    class NoValido(message: String) : ProductoError("ERROR: Producto no válido: $message")
}