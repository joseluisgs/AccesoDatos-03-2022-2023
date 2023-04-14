package es.joseluisgs.p06encordadosspringdatakotlin.models

import es.joseluisgs.p06encordadosspringdatakotlin.extensions.toLocalDate
import es.joseluisgs.p06encordadosspringdatakotlin.extensions.toLocalMoney
import es.joseluisgs.p06encordadosspringdatakotlin.extensions.toLocalNumber
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.Type
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "tenistas") // Solo es necesario si cambia la tabla respecto al modelo
data class Tenista(
    @Id @GeneratedValue
    @Column(name = "uuid")
    @Type(type = "uuid-char")
    val uuid: UUID = UUID.randomUUID(),
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
    // Relación 1-N con Raqueta, un tenista puede tener una raqueta y una raqueta puede tener muchos tenistas
    @ManyToOne
    @JoinColumn(name = "raqueta_uuid", referencedColumnName = "uuid", nullable = true)
    var raqueta: Raqueta? = null, // No tiene por que tener raqueta

    @Column(name = "created_at")
    @CreationTimestamp
    val createdAt: LocalDateTime = LocalDateTime.now(),
) {

    override fun toString(): String {
        return "Tenista(uuid=$uuid, nombre='$nombre', ranking=$ranking, " +
                "fechaNacimiento=${fechaNacimiento.toLocalDate()}, " +
                "añoProfesional=$añoProfesional, " +
                "altura=${(altura.toDouble() / 100).toLocalNumber()} cm, " +
                "peso=$peso Kg, " +
                "ganancias=${ganancias.toLocalMoney()}, " +
                "manoDominante=${manoDominante.mano}, " +
                "tipoReves=${tipoReves.tipo}, " +
                "puntos=$puntos, pais=$pais, " +
                // Cuidado con la recursividad, porque al imprimir la raqueta, esta a su vez imprime el tenista y
                // así sucesivamente entramos en un bucle infinito
                "raqueta=${raqueta?.marca})"
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