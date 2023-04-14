package repositories.ventas

import mappers.toLineaVenta
import mappers.toVenta
import models.Venta
import mu.KotlinLogging
import org.koin.core.annotation.Singleton
import services.database.SqlDeLightClient
import java.time.LocalDateTime

private val logger = KotlinLogging.logger {}

@Singleton
class VentasRepositoryImpl(
    val sqlClient: SqlDeLightClient,
) : VentasRepository {

    val db = sqlClient.productosQueries

    override fun findAll(): List<Venta> {
        logger.debug { "findAll()" }

        val ventas = db.selectAllVentas().executeAsList()
            .map { it.toVenta() }

        if (ventas.isNotEmpty()) {
            // Obtenemos las lineas de las ventas
            ventas.forEach { venta ->
                db.selectLineaVentaByVentaId(venta.id).executeAsList()
                    .map { it.toLineaVenta() }
                    .forEach { linea ->
                        venta.addLinea(linea)
                    }
            }
        }
        return ventas
    }

    override fun findById(id: Long): Venta? {
        logger.debug { "findById(${id})" }
        val venta = db.selectVentaById(id).executeAsOneOrNull()?.toVenta()
        if (venta != null) {
            // Obtenemos las lineas de las ventas
            db.selectLineaVentaByVentaId(venta.id).executeAsList()
                .map { it.toLineaVenta() }
                .forEach { linea ->
                    venta.addLinea(linea)
                }
        }
        return venta
    }

    override fun save(entity: Venta): Venta {
        logger.debug { "save: $entity" }
        // Lo trato todo como una transacción
        var myId = 0L
        val createdTime = LocalDateTime.now()
        db.transaction {
            db.insertVenta(
                entity.userId,
                createdTime.toString(),
                createdTime.toString(),
                entity.totalPrecio,
                entity.totalItems.toLong()
            )

            // Obtenemos el ID de la venta
            myId = db.selectVentaLastInserted().executeAsOne().id

            entity.lineas.forEach { linea ->
                db.insertLineaVenta(
                    myId,
                    linea.lineaId,
                    linea.productoId,
                    linea.cantidad.toLong(),
                    linea.productoPrecio,
                    linea.totalPrecio
                )
            }
        }
        return entity.copy(
            id = myId,
            createdAt = createdTime,
            updatedAt = createdTime,
            lineas = entity.lineas.map { it.copy(ventaId = myId) }.toMutableList()
        )
    }

    override fun update(entity: Venta): Venta {
        TODO("Not yet implemented")
    }

    override fun deleteById(id: Long): Boolean {
        TODO("Not yet implemented")
    }
}