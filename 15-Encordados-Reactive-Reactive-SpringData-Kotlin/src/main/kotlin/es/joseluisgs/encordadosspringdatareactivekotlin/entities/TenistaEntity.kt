package es.joseluisgs.encordadosspringdatareactivekotlin.entities

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDate
import java.util.*

// Al día de Hoy con Spring Data lo mejor es usar id autonuméricos
@Table(name = "TENISTAS") // Solo es necesario si cambia la tabla respecto al modelo
data class TenistaEntity(
    @Id
    var id: Long?,
    var nombre: String,
    var ranking: Int,
    @Column("fecha_nacimiento")
    var fechaNacimiento: LocalDate,
    @Column("año_profesional")
    var añoProfesional: Int,
    var altura: Int,
    var peso: Int,
    var ganancias: Double,
    @Column("mano_dominante")
    var manoDominante: String,
    @Column("tipo_reves")
    var tipoReves: String,
    var puntos: Int,
    var pais: String,
    @Column("raqueta_id")
    var raquetaId: Long? = null,
)