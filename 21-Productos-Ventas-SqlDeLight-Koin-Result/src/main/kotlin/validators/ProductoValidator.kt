package validators

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import errors.ProductoError
import models.Producto
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

fun Producto.validar(): Result<Producto, ProductoError> {
    logger.debug { "validar: $this" }

    if (nombre.isBlank()) return Err(ProductoError.ProductoNoValidoError("El nombre no puede estar vacío"))
    if (precio < 0) return Err(ProductoError.ProductoNoValidoError("El precio no puede ser negativo"))
    if (cantidad < 0) return Err(ProductoError.ProductoNoValidoError("La cantidad no puede ser negativa"))

    return Ok(this)
}