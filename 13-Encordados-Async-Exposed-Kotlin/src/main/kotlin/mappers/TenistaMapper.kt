package mappers

import entities.TenistasDao
import models.Tenista

// Mapeadores de dados para Tenista

fun TenistasDao.fromTenistaDaoToTenista(): Tenista {
    return Tenista(
        uuid = id.value,
        nombre = nombre,
        ranking = ranking,
        fechaNacimiento = fechaNacimiento,
        añoProfesional = añoProfesional,
        altura = altura,
        peso = peso,
        ganancias = ganancias,
        manoDominante = Tenista.ManoDominante.from(manoDominante),
        tipoReves = Tenista.TipoReves.from(tipoReves),
        puntos = puntos,
        pais = pais,
        raqueta = raqueta.fromRaquetaDaoToRaqueta(),
    )
}

