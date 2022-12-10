package entities

import org.ufoss.kotysa.h2.H2Table
import java.util.*

/**
 * Tabla de roles
 * CUIDADO con los tipos d datos que faltan, por ejemplo faltaba en esta version el DOUBLE
 * https://ufoss.org/kotysa/table-mapping.html#h2
 */
object RaquetasTable : H2Table<RaquetaEntity>("raquetas") {
    val uuid = uuid(RaquetaEntity::uuid).primaryKey()
    val marca = varchar(RaquetaEntity::marca, size = 100)
    val precio = varchar(RaquetaEntity::precio) // No tenemos double??
}

// El DTO de la base de datos
data class RaquetaEntity(
    val uuid: UUID,
    val marca: String,
    val precio: String,
)

