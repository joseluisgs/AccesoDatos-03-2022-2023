package entities

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import java.util.*

// Tabla de Raquetas
object RaquetasTable : UUIDTable("RAQUETAS") {
    val marca = varchar("marca", 100)
    val precio = double("precio")
}

// DAO de RaquetasTable
class RaquetasDao(id: EntityID<UUID>) : UUIDEntity(id) {
    // mi id será el de la tabla...
    companion object : UUIDEntityClass<RaquetasDao>(RaquetasTable)

    var marca by RaquetasTable.marca
    var precio by RaquetasTable.precio

    // Relación inversa donde soy referenciado (tenistas) referrersOn, solo si quiero acceder a ellos
    // Si le pongo val no dejo asignar tenistas desde aquí
    // 1 - M (1 raqueta - muchos tenistas que la usan)
    val tenistas by TenistasDao referrersOn TenistasTable.raqueta
}