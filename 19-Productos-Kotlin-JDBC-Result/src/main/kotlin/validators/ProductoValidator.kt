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

    if (nombre.isBlank()) return Err(ProductoError.NoValido("Nombre no puede estar vacío"))
    if (precio < 0) return Err(ProductoError.NoValido("Precio debe ser mayor o igual a 0"))
    if (cantidad < 0) return Err(ProductoError.NoValido("Cantidad debe ser mayor o igual a 0"))

    return Ok(this)
}