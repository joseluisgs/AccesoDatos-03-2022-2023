package repository.raqueta

import db.DataBaseManager
import model.Raqueta
import mu.KotlinLogging
import java.util.*

private val logger = KotlinLogging.logger {}

class RaquetasRepositoryImpl : RaquetasRepository {
    // Lista todos los tenistas
    override fun findAll(): List<Raqueta> {
        // Creamos la lista de tenistas
        val raquetas = mutableListOf<Raqueta>()
        // Creamos la consulta
        val query = "SELECT * FROM raquetas"
        // Ejecutamos la consulta
        DataBaseManager.use { db ->
            val result = db.select(query)
            // Recorremos el resultado
            result?.let {
                while (result.next()) {
                    // Creamos raqueta
                    val raqueta = Raqueta(
                        uuid = it.getObject("uuid") as UUID,
                        marca = it.getString("marca"),
                        precio = it.getDouble("precio")
                    )
                    // Añadimos el tenista a la lista
                    raquetas.add(raqueta)
                }
            }
        }
        // Devolvemos la lista de tenistas
        logger.debug { "Raquetas encontradas: ${raquetas.size}" }
        return raquetas.toList()
    }

    override fun findById(id: UUID): Raqueta? {
        // Creamos el tenista
        var raqueta: Raqueta? = null
        // Creamos la consulta
        val query = "SELECT * FROM raquetas WHERE uuid = ?"
        // Ejecutamos la consulta
        DataBaseManager.use { db ->
            val result = db.select(query, id)

            // Recorremos el resultado
            result?.let {
                if (result.next()) {
                    // Creamos el tenista
                    raqueta = Raqueta(
                        uuid = it.getObject("uuid") as UUID,
                        marca = it.getString("marca"),
                        precio = it.getDouble("precio")
                    )
                }
            }
        }
        // Devolvemos la lista de tenistas
        logger.debug { "Raqueta encontrada con $id: $raqueta" }
        return raqueta
    }

    override fun save(entity: Raqueta): Raqueta {
        // Si no existe salvamos si existe actualizamos
        // Let run es un if else pero en una sola línea, es más ideomático en Kotlin, no te asustes y usa to if else con null ;)
        val tenista = findById(entity.uuid)
        tenista?.let {
            // Actualizamos
            return update(entity)
        } ?: run {
            // Salvamos
            return insert(entity)
        }
    }

    private fun insert(raqueta: Raqueta): Raqueta {
        var result = -1
        // Creamos la consulta
        val query = """INSERT INTO raquetas 
            (uuid, marca, precio)
            VALUES (?, ?, ?)"""
            .trimIndent()
        // Ejecutamos la consulta
        DataBaseManager.use { db ->
            result = db.insert(
                query, raqueta.uuid, raqueta.marca, raqueta.precio
            )
        }
        // Devolvemos el raqueta
        logger.debug { "Raqueta insertada: $raqueta - Resultado: ${result == 1}" }
        return raqueta
    }

    private fun update(raqueta: Raqueta): Raqueta {
        var result = -1
        // Creamos la consulta
        val query = """UPDATE raquetas
            SET marca = ?, precio = ?
            WHERE uuid = ?"""
            .trimIndent()
        // Ejecutamos la consulta
        DataBaseManager.use { db ->
            result = db.update(
                query, raqueta.marca, raqueta.precio, raqueta.uuid
            )
        }
        // Devolvemos el tenista
        logger.debug { "Raqueta actualizada: $raqueta - Resultado: ${result == 1}" }
        return raqueta
    }

    override fun delete(entity: Raqueta): Boolean {
        // si hay tenistas con esa raqueta no se puede borrar -- No es nuestro caso, porque permitimos nulos
        // val tenistas = raquetasRepository.cuantosTenistas(raqueta)
        // require(tenistas != 0) { "No se puede borrar la raqueta porque hay tenistas que la usan" }

        // como la raqueta puede ser nula, debemos tener en cuenta que
        // si borramos la raqueta de un tenista, el tenista queda sin raqueta y es nula,
        // hay que actualizarla
        // lo voy a hacer con una transacción
        var result: Int = -1
        DataBaseManager.use { db ->
            db.transaction {
                // deleteRaquetaToTenistas(entity)
                // Si hubiesemos decidido un borrado en cascada (que no es nuestro caso)
                // habría que borrar los tenistas, pero no vamos a usar ondelete cascade en la base de datos
                // deleteTenistasDeRaqueta(entity)
                // Creamos la consulta que hace lo anterior para la transacción
                // Para los tenistas
                var query = "DELETE FROM tenistas WHERE raqueta_uuid = ?"
                db.delete(query, entity.uuid)
                logger.debug { "Eliminando tenistas asociados" }

                query = "DELETE FROM raquetas WHERE uuid = ?"
                // Ejecutamos la consulta
                result = db.delete(query, entity.uuid)
                // Cerramos la conexión
            }
        }
        // Devolvemos el resultado
        logger.debug { "Raqueta eliminada: $entity - Resultado: ${result == 1}" }
        return result == 1
    }

    private fun deleteTenistasDeRaqueta(entity: Raqueta) {
        var result: Int
        logger.debug { "Eliminando tenistas asociados" }
        // Creamos la consulta
        val query = "DELETE FROM tenistas WHERE raqueta_uuid = ?"
        // Ejecutamos la consulta
        DataBaseManager.use { db ->
            result = db.delete(query, entity.uuid)
        }
        logger.debug { "Tenistas eliminados: $result" }
    }

    private fun cuantosTenistas(entity: Raqueta): Int {
        var cuantos: Int = 0
        // Creamos la consulta
        val query = "SELECT COUNT(*) FROM tenistas WHERE raqueta_uuid = ?"
        // Ejecutamos la consulta
        DataBaseManager.use { db ->
            val result = db.select(query, entity.uuid)
            // Creamos el tenista

            // Recorremos el resultado
            result?.let {
                if (result.next()) {
                    // Creamos el tenista
                    cuantos = it.getInt(1)
                }
            }
        }
        // Devolvemos la lista de tenistas
        logger.debug { "Tenistas encontrados con raqueta $entity: $cuantos" }
        return cuantos
    }

    private fun deleteRaquetaToTenistas(entity: Raqueta): Boolean {
        var result: Int
        logger.debug { "Actualizando a null la raquetas a tenistas" }
        // Creamos la consulta
        val query = "UPDATE tenistas SET raqueta_uuid = NULL WHERE raqueta_uuid = ?"
        // Ejecutamos la consulta
        DataBaseManager.use { db ->
            result = db.update(query, entity.uuid)
            // Cerramos la conexión
        }
        // Devolvemos el resultado
        logger.debug { "Raqueta eliminada de los tenistas: $entity - Resultado: ${result == 1}" }
        return result == 1
    }
}