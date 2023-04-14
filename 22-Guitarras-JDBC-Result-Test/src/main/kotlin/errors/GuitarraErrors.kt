package errors

sealed class GuitarraError(val message: String) {
    class MarcaNoValida(message: String) : GuitarraError(message)
    class ModeloNoValido(message: String) : GuitarraError(message)
    class PrecioNoValido(message: String) : GuitarraError(message)
    class StockNoValido(message: String) : GuitarraError(message)
    class GuitarraNoEncontrada(message: String) : GuitarraError(message)
}