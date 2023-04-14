package repositories.productos

import mappers.toProducto
import models.Producto
import mu.KotlinLogging
import org.koin.core.annotation.Singleton
import services.database.SqlDeLightClient
import java.time.LocalDateTime

private val logger = KotlinLogging.logger {}

@Singleton
class ProductosRepositoryImpl(
    val sqlClient: SqlDeLightClient,
) : ProductosRepository {

    val db = sqlClient.productosQueries

    override fun findAll(): List<Producto> {
        logger.debug { "findAll" }
        return db.selectAllProductos().executeAsList()
            .map { it.toProducto() }
    }


    override fun findById(id: Long): Producto? {
        logger.debug { "findById $id" }
        return db.selectProductoById(id).executeAsOneOrNull()
            ?.toProducto()
    }

    override fun findAllByDisponible(disponible: Boolean): List<Producto> {
        logger.debug { "findAllByDisponible: $disponible" }
        return db.selectAllProductosByDisponible(if (disponible) 1 else 0).executeAsList()
            .map { it.toProducto() }
    }

    override fun findByNombre(nombre: String): List<Producto> {
        logger.debug { "findByNombre: $nombre" }
        return findAll().filter { it.nombre.lowercase().contains(nombre.lowercase()) }
    }

    override fun save(entity: Producto): Producto {
        logger.debug { "save $entity" }
        val createdTime = LocalDateTime.now()
        db.transaction {
            db.insertProducto(
                nombre = entity.nombre,
                precio = entity.precio,
                cantidad = entity.cantidad.toLong(),
                created_at = createdTime.toString(),
                updated_at = createdTime.toString(),
                disponible = if (entity.disponible) 1 else 0
            )
        }
        return db.selectProductoLastInserted().executeAsOne()
            .toProducto()
    }

    override fun update(entity: Producto): Producto {
        logger.debug { "update $entity" }
        val updatedTime = LocalDateTime.now()
        db.updateProducto(
            nombre = entity.nombre,
            precio = entity.precio,
            cantidad = entity.cantidad.toLong(),
            updated_at = updatedTime.toString(),
            disponible = if (entity.disponible) 1 else 0,
            id = entity.id,
        )
        return entity.copy(updatedAt = updatedTime)
    }

    override fun deleteById(id: Long): Boolean {
        logger.debug { "deleteById $id" }
        db.deleteProducto(id)
        return true
    }

    override fun deleteAll() {
        logger.debug { "deleteAll" }
        db.transaction {
            db.removeAllProductos()
        }
    }
}

