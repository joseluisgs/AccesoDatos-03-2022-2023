package models

import extensions.toLocalDate
import extensions.toLocalMoney
import extensions.toLocalNumber
import kotlinx.serialization.Serializable
import serializers.LocalDateTimeSerializer
import serializers.UUIDSerializer
import java.time.LocalDate
import java.util.*

/**
 * Modelo
 * CUIDADO con los tipos d datos que faltan, por ejemplo faltaba en esta version el DOUBLE
 * https://ufoss.org/kotysa/table-mapping.html#h2
 * Lo ideal seria un DTD que luego cambie los datos a lo que quiero, peor no voy a liarlo más
 */

@Serializable // por lo lo queremos serializar/deseralizar en JSON
data class Tenista(
    @Serializable(with = UUIDSerializer::class)
    val uuid: UUID = UUID.randomUUID(),
    var nombre: String,
    var ranking: Int,
    @Serializable(with = LocalDateTimeSerializer::class)
    var fechaNacimiento: LocalDate,
    var añoProfesional: Int,
    var altura: Int,
    var peso: Int,
    var ganancias: Double,
    var manoDominante: ManoDominante,
    var tipoReves: TipoReves,
    var puntos: Int,
    var pais: String,
    @Serializable(with = UUIDSerializer::class)
    var raqueta: Raqueta? = null, // No tiene por que tener raqueta
) {

    override fun toString(): String {
        return "Tenista(uuid=$uuid, nombre='$nombre', ranking=$ranking, " +
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