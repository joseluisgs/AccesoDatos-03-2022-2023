package models

import locale.toLocalDateTime
import locale.toLocalMoney
import java.time.LocalDateTime
import java.util.*

data class Producto(
    val id: Long = 0,
    val uuid: UUID = UUID.randomUUID(),
    val nombre: String = "",
    val precio: Double = 0.0,
    val cantidad: Int = 0,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
    val disponible: Boolean = true,
) {
    fun toLocalString(): String {
        return "Producto(id=$id, nombre='$nombre', precio=${precio.toLocalMoney()}, cantidad=$cantidad, , disponible=$disponible, uuid=${uuid}, createdAt=${createdAt.toLocalDateTime()}, updatedAt=${updatedAt.toLocalDateTime()})"
    }
}
