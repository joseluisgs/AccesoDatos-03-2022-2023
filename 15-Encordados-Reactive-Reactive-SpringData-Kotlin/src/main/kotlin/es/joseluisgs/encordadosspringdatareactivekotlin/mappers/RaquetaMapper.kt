package mappers

import es.joseluisgs.encordadosspringdatareactivekotlin.entities.RaquetaEntity
import es.joseluisgs.encordadosspringdatareactivekotlin.models.Raqueta

fun RaquetaEntity.toRaqueta(): Raqueta {
    return Raqueta(
        id = id,
        marca = marca,
        precio = precio,
    )
}

fun Raqueta.toRaquetaEntity(): RaquetaEntity {
    return RaquetaEntity(
        id = id,
        marca = marca,
        precio = precio,
    )
}




