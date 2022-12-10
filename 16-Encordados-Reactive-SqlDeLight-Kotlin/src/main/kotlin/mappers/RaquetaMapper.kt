package mappers

import models.Raqueta
import database.Raqueta as RaquetaEntity

fun RaquetaEntity.toRaqueta(): Raqueta {
    return Raqueta(
        id = this.id,
        marca = this.marca,
        precio = this.precio,
    )
}

fun Raqueta.toRaquetaEntity(): RaquetaEntity {
    return RaquetaEntity(
        id = this.id,
        marca = this.marca,
        precio = this.precio,
    )
}