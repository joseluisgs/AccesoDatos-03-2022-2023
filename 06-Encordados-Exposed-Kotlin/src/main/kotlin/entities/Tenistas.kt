package entities

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.javatime.date
import java.util.*

// Tabla de Tenistas
object TenistasTable : UUIDTable() {
    val nombre = varchar("nombre", 100)
    val ranking = integer("ranking")
    val fechaNacimiento = date("fecha_nacimiento")
    val añoProfesional = integer("año_profesional")
    val altura = integer("altura")
    val peso = integer("peso")
    val ganancias = double("ganancias")
    val manoDominante = varchar("mano_dominante", 20)
    val tipoReves = varchar("tipo_reves", 20)
    val puntos = integer("puntos")
    val pais = varchar("pais", 100)

    // Mi raqueta, un tenista lleva un modelo de raqueta, es referenciada por el id de la raqueta clave de raqueta se propaga en la tabla tenistas
    // M a 1 con raquetas, por eso se propaga el id de raqueta
    // onDelete cascade
    // ReferenceOption.SET_NULL --> Si se elimina la raqueta, el tenista no se elimina, se pone a null
    // ReferenceOption.CASCADE --> Si se elimina la raqueta, se elimina el tenista
    // ReferenceOption.RESTRICT --> Si se elimina la raqueta, no se puede eliminar mientras haya tenistas con esa raqueta
    // ReferenceOption.NO_ACTION --> Si se elimina la raqueta, no se puede eliminar mientras haya tenistas con esa raqueta
    val raqueta = reference("raqueta_id", RaquetasTable) // si fuera nulo añadiriamos .nullable()
    // Si quisieramos que la raqueta nulo
    // val raqueta = reference("raqueta_id", onDelete = ReferenceOption.SET_NULL, RaquetasTable).nullable()
}


// DAO de Tenista Clase que mapea la fila de la tabla Tenistas con id uuid
class TenistasDao(id: EntityID<UUID>) : UUIDEntity(id) {

    // mi id será el de la tabla...
    companion object : UUIDEntityClass<TenistasDao>(TenistasTable)

    // mis propiedades con qué columnas de la tabla corresponden
    var nombre by TenistasTable.nombre
    var ranking by TenistasTable.ranking
    var fechaNacimiento by TenistasTable.fechaNacimiento
    var añoProfesional by TenistasTable.añoProfesional
    var altura by TenistasTable.altura
    var peso by TenistasTable.peso
    var ganancias by TenistasTable.ganancias
    var manoDominante by TenistasTable.manoDominante
    var tipoReves by TenistasTable.tipoReves
    var puntos by TenistasTable.puntos
    var pais by TenistasTable.pais

    // Clave externa a raqueta -- Debemos tener en cuenta los probelmas de la direccionalidad...
    // Si le pongo var dejo asignar la raqueta desde la tabla tenista.
    var raqueta by RaquetasDao referencedOn TenistasTable.raqueta

}