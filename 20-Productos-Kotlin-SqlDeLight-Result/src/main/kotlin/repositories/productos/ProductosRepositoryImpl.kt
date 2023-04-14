package repositories.productos

import mappers.toProducto
import models.Producto
import mu.KotlinLogging
import services.SqlDeLightClient
import java.time.LocalDateTime

private val logger = KotlinLogging.logger {}

class ProductosRepositoryImpl : ProductosRepository {
    private val db = SqlDeLightClient.productosQueries

    override fun findAll(): List<Producto> {
        logger.debug { "findAll" }
        return db.selectAll().executeAsList().map { it.toProducto() }
    }

    override fun findById(id: Long): Producto? {
        logger.debug { "findById $id" }
        return db.selectById(id).executeAsOneOrNull()?.toProducto()
    }

    override fun findByUuid(uuid: String): Producto? {
        logger.debug { "findByUuid $uuid" }
        return db.selectByUuid(uuid).executeAsOneOrNull()?.toProducto()
    }

    override fun findByNombre(nombre: String): List<Producto> {
        logger.debug { "findByNombre $nombre" }
        return findAll().filter { it.nombre.lowercase().contains(nombre.lowercase()) }
    }

    override fun save(entity: Producto): Producto {
        logger.debug { "save $entity" }
        // Abrimos la conexión a la base de datos
        val createdTime = LocalDateTime.now()
        db.transaction {
            db.insert(
                uuid = entity.uuid.toString(),
                nombre = entity.nombre,
                precio = entity.precio,
                cantidad = entity.cantidad.toLong(),
                created_at = createdTime.toString(), // Lo meto yo ahora porque creo
                updated_at = createdTime.toString(), // Lo meto yo ahora porque creo
                disponible = if (entity.disponible) 1 else 0
            )
        }
        return db.selectLastInserted().executeAsOne().toProducto()
    }

    override fun update(entity: Producto): Producto {
        logger.debug { "update $entity" }
        val updatedTime = LocalDateTime.now()
        db.update(
            nombre = entity.nombre,
            precio = entity.precio,
            cantidad = entity.cantidad.toLong(),
            updated_at = updatedTime.toString(), // Lo meto yo ahora porque creo
            disponible = if (entity.disponible) 1 else 0,
            id = entity.id
        )
        return entity.copy(updatedAt = updatedTime)
    }

    override fun deleteById(id: Long): Boolean {
        logger.debug { "deleteById $id" }
        db.delete(id)
        return true
    }
}