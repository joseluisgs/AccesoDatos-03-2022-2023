package mappers

import entities.TenistaEntity
import models.Raqueta
import models.Tenista

// Mapeadores de dados para Tenista

fun TenistaEntity.toTenista(raquetaNew: Raqueta?): Tenista {
    return Tenista(
        uuid = uuid,
        nombre = nombre,
        ranking = ranking,
        fechaNacimiento = fechaNacimiento,
        añoProfesional = añoProfesional,
        altura = altura,
        peso = peso,
        ganancias = ganancias.toDouble(),
        manoDominante = Tenista.ManoDominante.from(manoDominante),
        tipoReves = Tenista.TipoReves.from(tipoReves),
        puntos = puntos,
        pais = pais,
        raqueta = raquetaNew
    )
}

fun Tenista.toTenistaEntity(): TenistaEntity {
    return TenistaEntity(
        uuid = uuid,
        nombre = nombre,
        ranking = ranking,
        fechaNacimiento = fechaNacimiento,
        añoProfesional = añoProfesional,
        altura = altura,
        peso = peso,
        ganancias = ganancias.toString(),
        manoDominante = manoDominante.mano,
        tipoReves = tipoReves.tipo,
        puntos = puntos,
        pais = pais,
        raquetaUuid = raqueta?.uuid,
    )
}

