package models

import locale.toLocalMoney

data class LineaVenta(
    val ventaId: Long, // No la necesitamos porque no estamos guardando la linea en la BD
    val lineaId: Long,
    val productoId: Long,
    val cantidad: Int,
    val productoPrecio: Double
) {
    val totalPrecio: Double
        get() = productoPrecio * cantidad

    fun toLocalString(): String {
        return "LineaVenta(idLineaVenta=$lineaId, idProducto=$productoId, cantidad=$cantidad, precioProducto=${productoPrecio.toLocalMoney()}, total=${totalPrecio.toLocalMoney()})"
    }
}