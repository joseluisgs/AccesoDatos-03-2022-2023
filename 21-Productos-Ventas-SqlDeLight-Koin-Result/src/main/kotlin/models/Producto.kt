package models

import locale.toLocalDateTime
import locale.toLocalMoney
import java.time.LocalDateTime

data class Producto(
    val id: Long = 0,
    val nombre: String = "",
    val precio: Double = 0.0,
    val cantidad: Int = 0,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
    val disponible: Boolean = true,
) {
    fun toLocalString(): String {
        return "Producto(id=$id, nombre='$nombre', precio=${precio.toLocalMoney()}, cantidad=$cantidad, , disponible=$disponible, createdAt=${createdAt.toLocalDateTime()}, updatedAt=${updatedAt.toLocalDateTime()})"
    }
}
