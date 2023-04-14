package dto

data class GuitarraDto(
    val id: Long = 0L,
    val uuid: String,
    val marca: String,
    val modelo: String,
    val precio: Double,
    val stock: Int,
    val createdAt: String,
    val updatedAt: String,
    val deleted: Boolean = false
)