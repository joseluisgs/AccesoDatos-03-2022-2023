package errors


// AL poner las llaves los puedo usar como un tipo enum, pero con mas flexibilidad
sealed class ProductoError(val message: String) {
    class ProductoNoEncontradoError(message: String) : ProductoError("ERROR: Producto no encontrado: $message")
    class ProductoNoGuardadoError(message: String) : ProductoError("ERROR: Producto no guardado: $message")
    class ProductoNoValidoError(message: String) : ProductoError("ERROR: Producto no válido: $message")
}
