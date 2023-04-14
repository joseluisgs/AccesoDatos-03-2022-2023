package errors

// Errores de dominio
sealed class ProductoError(val message: String) {
    class ProductoNoEncontradoError(message: String) : ProductoError("ERROR: Producto no encontrado: $message")
    class ProductoNoDisponibleError(message: String) : ProductoError("ERROR: Producto no disponible: $message")
    class ProductoNoValidoError(message: String) : ProductoError("ERROR: Producto no válido: $message")
}
