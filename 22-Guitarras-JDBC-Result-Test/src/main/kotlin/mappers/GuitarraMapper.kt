package mappers

import dto.GuitarraDto
import models.Guitarra

fun Guitarra.toGuitarraDto() = GuitarraDto(
    id = this.id,
    uuid = this.uuid.toString(),
    marca = this.marca,
    modelo = this.modelo,
    precio = this.precio,
    stock = this.stock,
    createdAt = this.createdAt.toString(),
    updatedAt = this.updatedAt.toString(),
    deleted = this.deleted
)

