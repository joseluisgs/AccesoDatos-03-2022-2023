package validators

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import errors.VentaError
import models.Venta
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

fun Venta.validar(items: Map<Long, Int>): Result<Venta, VentaError> {
    logger.debug { "validar: $this" }

    if (items.isEmpty()) return Err(VentaError.VentaNoValidoError("La venta no tiene items"))
    if (userId <= 0) return Err(VentaError.VentaNoValidoError("El usuario no es válido"))

    return Ok(this)
}