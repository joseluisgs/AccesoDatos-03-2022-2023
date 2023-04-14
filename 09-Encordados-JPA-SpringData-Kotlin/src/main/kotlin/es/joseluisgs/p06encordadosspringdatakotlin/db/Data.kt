package es.joseluisgs.p06encordadosspringdatakotlin.db

import es.joseluisgs.p06encordadosspringdatakotlin.models.Raqueta
import es.joseluisgs.p06encordadosspringdatakotlin.models.Representante
import es.joseluisgs.p06encordadosspringdatakotlin.models.Tenista
import java.time.LocalDate

fun getRepresentantesInit() = listOf(
    Representante("Juan Perez", "juan@perez.com"),
    Representante("Jorge Luis", "jorge@luis.com"),
    Representante("Ana Lopez", "ana@lopez.com")
)

fun getRaquetasInit() = listOf(
    Raqueta(
        marca = "Babolat",
        precio = 200.0
    ),
    Raqueta(
        marca = "Wilson",
        precio = 250.0
    ),
    Raqueta(
        marca = "Head",
        precio = 225.0
    ),
)

fun getTenistasInit() = listOf(
    Tenista(
        nombre = "Rafael Nadal",
        ranking = 2,
        fechaNacimiento = LocalDate.parse("1985-06-04"),
        añoProfesional = 2005,
        altura = 185,
        peso = 81,
        ganancias = 10000000.0,
        manoDominante = Tenista.ManoDominante.IZQUIERDA,
        tipoReves = Tenista.TipoReves.DOS_MANOS,
        puntos = 6789,
        pais = "España",
    ),
    Tenista(
        nombre = "Roger Federer",
        ranking = 3,
        fechaNacimiento = LocalDate.parse("1981-01-01"),
        añoProfesional = 2000,
        altura = 188,
        peso = 83,
        ganancias = 20000000.0,
        manoDominante = Tenista.ManoDominante.DERECHA,
        tipoReves = Tenista.TipoReves.UNA_MANO,
        puntos = 3789,
        pais = "Suiza",
    ),
    Tenista(
        nombre = "Novak Djokovic",
        ranking = 4,
        fechaNacimiento = LocalDate.parse("1986-05-05"),
        añoProfesional = 2004,
        altura = 189,
        peso = 81,
        ganancias = 10000000.0,
        manoDominante = Tenista.ManoDominante.DERECHA,
        tipoReves = Tenista.TipoReves.DOS_MANOS,
        puntos = 1970,
        pais = "Serbia",
    ),
    Tenista(
        nombre = "Dominic Thiem",
        ranking = 5,
        fechaNacimiento = LocalDate.parse("1985-06-04"),
        añoProfesional = 2015,
        altura = 188,
        peso = 82,
        ganancias = 10000.0,
        manoDominante = Tenista.ManoDominante.DERECHA,
        tipoReves = Tenista.TipoReves.UNA_MANO,
        puntos = 1234,
        pais = "Austria",
    ),
    Tenista(
        nombre = "Carlos Alcaraz",
        ranking = 1,
        fechaNacimiento = LocalDate.parse("2003-05-05"),
        añoProfesional = 2019,
        altura = 185,
        peso = 80,
        ganancias = 5000000.0,
        manoDominante = Tenista.ManoDominante.DERECHA,
        tipoReves = Tenista.TipoReves.DOS_MANOS,
        puntos = 6880,
        pais = "España",
    ),
)
