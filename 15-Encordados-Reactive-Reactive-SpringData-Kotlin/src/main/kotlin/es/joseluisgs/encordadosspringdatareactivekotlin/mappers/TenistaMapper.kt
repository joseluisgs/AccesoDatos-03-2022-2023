package mappers

import es.joseluisgs.encordadosspringdatareactivekotlin.entities.TenistaEntity
import es.joseluisgs.encordadosspringdatareactivekotlin.models.Raqueta
import models.Tenista

// Mapeadores de dados para Tenista

fun TenistaEntity.toTenista(raquetaNew: Raqueta?): Tenista {
    return Tenista(
        id = id,
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
    println("Tenista a convertir $this")
    return TenistaEntity(
        id = this.id,
        nombre = nombre,
        ranking = ranking,
        fechaNacimiento = fechaNacimiento,
        añoProfesional = añoProfesional,
        altura = altura,
        peso = peso,
        ganancias = ganancias,
        manoDominante = manoDominante.mano,
        tipoReves = tipoReves.tipo,
        puntos = puntos,
        pais = pais,
        raquetaId = raqueta?.id,
    )
}

