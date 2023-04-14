package mappers

import database.Linea_VentaEntity
import database.VentaEntity
import models.LineaVenta
import models.Venta
import java.time.LocalDateTime

fun VentaEntity.toVenta() = Venta(
    id = id,
    userId = user_id,
    createdAt = LocalDateTime.parse(created_at.toString()),
    updatedAt = LocalDateTime.parse(updated_at.toString()),
)

fun Linea_VentaEntity.toLineaVenta() = LineaVenta(
    ventaId = venta_id,
    lineaId = linea_id,
    productoId = producto_id,
    cantidad = cantidad.toInt(),
    productoPrecio = producto_precio,
)