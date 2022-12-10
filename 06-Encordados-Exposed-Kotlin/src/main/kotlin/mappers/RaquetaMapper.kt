package mappers

import entities.RaquetasDao
import models.Raqueta

fun RaquetasDao.fromRaquetaDaoToRaqueta(): Raqueta {
    return Raqueta(
        uuid = id.value,
        marca = marca,
        precio = precio,
        //tenistas = tenistas.map { it.fromTenistaDaoToTenista() }
    )
}




