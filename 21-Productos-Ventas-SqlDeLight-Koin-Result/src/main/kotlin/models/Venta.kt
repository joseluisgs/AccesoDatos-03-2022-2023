package models

import locale.toLocalDateTime
import locale.toLocalMoney
import java.time.LocalDateTime

data class Venta(
    val id: Long = 0,
    val userId: Long,
    val lineas: MutableList<LineaVenta> = mutableListOf(),
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
) {

    val totalPrecio: Double
        get() = lineas.sumOf { it.totalPrecio }

    val totalItems: Int
        get() = lineas.sumOf { it.cantidad }

    val nextLineaId: Long
        get() = lineas.size + 1L

    fun addLinea(linea: LineaVenta) {
        lineas.add(linea)
    }

    fun removeLinea(idLineaCarrito: Long) {
        lineas.removeIf { it.lineaId == idLineaCarrito }
    }

    fun toLocalString(): String {
        return "Venta(id=$id, userId=$userId, totalItems=$totalItems, total=${totalPrecio.toLocalMoney()}, lineas=${
            lineas.joinToString(
                prefix = "[",
                postfix = "]"
            ) { it.toLocalString() }
        } , createdAt=${createdAt.toLocalDateTime()}, updatedAt=${updatedAt.toLocalDateTime()})"
    }
}