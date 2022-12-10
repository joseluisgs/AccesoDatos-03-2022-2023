package mappers

import models.Raqueta
import models.Tenista
import java.time.LocalDate
import database.Tenista as TenistaEntity

fun TenistaEntity.toTenista(raquetaNew: Raqueta?): Tenista {
    return Tenista(
        id = id,
        nombre = nombre,
        ranking = ranking.toInt(),
        fechaNacimiento = LocalDate.parse(fecha_nacimiento),
        añoProfesional = anno_profesional.toInt(),
        altura = altura.toInt(),
        peso = peso.toInt(),
        ganancias = ganancias.toDouble(),
        manoDominante = Tenista.ManoDominante.from(mano_dominante),
        tipoReves = Tenista.TipoReves.from(tipo_reves),
        puntos = puntos.toInt(),
        pais = pais,
        raqueta = raquetaNew
    )
}

fun Tenista.toTenistaEntity(): TenistaEntity {
    return TenistaEntity(
        id = id,
        nombre = nombre,
        ranking = ranking.toLong(),
        fecha_nacimiento = fechaNacimiento.toString(),
        anno_profesional = añoProfesional.toLong(),
        altura = altura.toLong(),
        peso = peso.toLong(),
        ganancias = ganancias,
        mano_dominante = manoDominante.mano,
        tipo_reves = tipoReves.tipo,
        puntos = puntos.toLong(),
        pais = pais,
        raqueta_id = raqueta?.id,
    )
}

