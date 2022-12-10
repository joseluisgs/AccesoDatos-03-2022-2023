package entities

import org.ufoss.kotysa.h2.H2Table
import java.time.LocalDate
import java.util.*

/**
 * Tabla de tenistas
 * CUIDADO con los tipos d datos que faltan, por ejemplo faltaba en esta version el DOUBLE
 * https://ufoss.org/kotysa/table-mapping.html#h2
 */
object TenistasTable : H2Table<TenistaEntity>("tenistas") {
    val uuid = uuid(TenistaEntity::uuid).primaryKey()
    val nombre = varchar(TenistaEntity::nombre, size = 100)
    val ranking = integer(TenistaEntity::ranking)
    val fechaNacimiento = date(TenistaEntity::fechaNacimiento, "fecha_nacimiento")
    val añoProfesional = integer(TenistaEntity::añoProfesional, "año_profesional")
    val altura = integer(TenistaEntity::altura)
    val peso = integer(TenistaEntity::peso)
    val ganancias = varchar(TenistaEntity::ganancias, size = 100)
    val manoDominante = varchar(TenistaEntity::manoDominante, "mano_dominante", 20)
    val tipoReves = varchar(TenistaEntity::tipoReves, "tipo_reves", 20)
    val puntos = integer(TenistaEntity::puntos)
    val pais = varchar(TenistaEntity::pais, size = 100)
    val raquetaUuid = uuid(TenistaEntity::raquetaUuid).foreignKey(RaquetasTable.uuid)
}

// El DTO de la base de datos
data class TenistaEntity(
    val uuid: UUID,
    var nombre: String,
    var ranking: Int,
    var fechaNacimiento: LocalDate,
    var añoProfesional: Int,
    var altura: Int,
    var peso: Int,
    var ganancias: String,
    var manoDominante: String,
    var tipoReves: String,
    var puntos: Int,
    var pais: String,
    var raquetaUuid: UUID? = null, // No tiene por que tener raqueta
)
