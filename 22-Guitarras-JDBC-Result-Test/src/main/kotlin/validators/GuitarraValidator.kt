package validators

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import errors.GuitarraError
import models.Guitarra


fun Guitarra.validate(): Result<Guitarra, GuitarraError> {
    return when {
        marca.isBlank() -> Err(GuitarraError.MarcaNoValida("La marca no puede estar vacía"))
        modelo.isBlank() -> Err(GuitarraError.ModeloNoValido("El modelo no puede estar vacío"))
        precio <= 0 -> Err(GuitarraError.PrecioNoValido("El precio no puede ser negativo"))
        stock <= 0 -> Err(GuitarraError.StockNoValido("El stock no puede ser negativo"))
        else -> Ok(this)
    }
}