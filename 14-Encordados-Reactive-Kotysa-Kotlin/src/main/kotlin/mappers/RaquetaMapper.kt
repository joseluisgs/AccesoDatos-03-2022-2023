package mappers

import entities.RaquetaEntity
import models.Raqueta

fun RaquetaEntity.toRaqueta(): Raqueta {
    return Raqueta(
        uuid = uuid,
        marca = marca,
        precio = precio.toDouble(),
    )
}

fun Raqueta.toRaquetaEntity(): RaquetaEntity {
    return RaquetaEntity(
        uuid = uuid,
        marca = marca,
        precio = precio.toString(),
    )
}




