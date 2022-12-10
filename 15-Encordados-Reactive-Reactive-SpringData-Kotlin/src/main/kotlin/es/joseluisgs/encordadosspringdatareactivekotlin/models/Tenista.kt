package models

import es.joseluisgs.encordadosspringdatareactivekotlin.extensions.toLocalDate
import es.joseluisgs.encordadosspringdatareactivekotlin.extensions.toLocalMoney
import es.joseluisgs.encordadosspringdatareactivekotlin.extensions.toLocalNumber
import es.joseluisgs.encordadosspringdatareactivekotlin.models.Raqueta
import java.time.LocalDate

/**
 * Modelo
 * CUIDADO con los tipos d datos que faltan, por ejemplo faltaba en esta version el DOUBLE
 * https://ufoss.org/kotysa/table-mapping.html#h2
 * Lo ideal seria un DTD que luego cambie los datos a lo que quiero, peor no voy a liarlo más
 * // Al día de Hoy con Spring Data r2dbc lo mejor es usar id autonuméricos
 */

data class Tenista(
    var id: Long? = null,
    var nombre: String,
    var ranking: Int,
    var fechaNacimiento: LocalDate,
    var añoProfesional: Int,
    var altura: Int,
    var peso: Int,
    var ganancias: Double,
    var manoDominante: ManoDominante,
    var tipoReves: TipoReves,
    var puntos: Int,
    var pais: String,
    var raqueta: Raqueta? = null, // No tiene por que tener raqueta
) {

    override fun toString(): String {
        return "Tenista(id=$id, nombre='$nombre', ranking=$ranking, " +
                "fechaNacimiento=${fechaNacimiento.toLocalDate()}, " +
                "añoProfesional=$añoProfesional, " +
                "altura=${(altura.toDouble() / 100).toLocalNumber()} cm, " +
                "peso=$peso Kg, " +
                "ganancias=${ganancias.toLocalMoney()}, " +
                "manoDominante=${manoDominante}, " +
                "tipoReves=${tipoReves.tipo}, " +
                "puntos=$puntos, pais=$pais, " +
                "raqueta=${raqueta})"
    }


    // ENUMS de la propia clase
    enum class ManoDominante(val mano: String) {
        DERECHA("DERECHA"),
        IZQUIERDA("IZQUIERDA");

        companion object {
            fun from(manoDominante: String): ManoDominante {
                return when (manoDominante.uppercase()) {
                    "DERECHA" -> DERECHA
                    "IZQUIERDA" -> IZQUIERDA
                    else -> throw IllegalArgumentException("ManoDominante no reconocida")
                }
            }
        }
    }

    enum class TipoReves(val tipo: String) {
        UNA_MANO("UNA MANO"),
        DOS_MANOS("DOS MANOS");

        companion object {
            fun from(tipoReves: String): TipoReves {
                return when (tipoReves.uppercase()) {
                    "UNA MANO" -> UNA_MANO
                    "DOS MANOS" -> DOS_MANOS
                    else -> throw IllegalArgumentException("TipoReves no reconocida")
                }
            }
        }
    }


}