package models

import locale.toLocalDateTime
import locale.toLocalMoney
import java.time.LocalDateTime
import java.util.*

data class Guitarra(
    val id: Long = 0L,
    val uuid: UUID = UUID.randomUUID(),
    val marca: String,
    val modelo: String,
    val precio: Double,
    val stock: Int,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
    val deleted: Boolean = false
) {
    fun toStringLocale(): String {
        return "Guitarra(id=$id, uuid=$uuid, marca='$marca', modelo='$modelo', precio=${precio.toLocalMoney()}, stock=$stock, createdAt=${createdAt.toLocalDateTime()}, updatedAt=${updatedAt.toLocalDateTime()}, deleted=$deleted)"
    }
}