package controllers

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.andThen
import errors.VentaError
import models.LineaVenta
import models.Venta
import mu.KotlinLogging
import org.koin.core.annotation.Singleton
import repositories.productos.ProductosRepository
import repositories.ventas.VentasRepository
import services.storage.ventas.VentasStorageService
import validators.validar

private val logger = KotlinLogging.logger {}

@Singleton
class VentasController(
    private val ventasRepository: VentasRepository,
    private val productosRepository: ProductosRepository,
    private val exportStorage: VentasStorageService
) {
    fun save(userId: Long, items: Map<Long, Int>): Result<Venta, VentaError> {
        logger.info { "save: $userId, $items" }
        val venta = Venta(id = 0, userId = userId)
        // Validar
        return venta.validar(items)
            .andThen {
                // Creamos las lineas de carrito
                comprobarExistenciaProductos(items)
            }.andThen {
                // añadir lineas al carrito
                crearLineasCarrito(venta, items)
            }.andThen {
                // actualizar stock
                actualizarStock(items)
            }.andThen {
                // guardar venta
                Ok(ventasRepository.save(venta))
            }
    }

    private fun crearLineasCarrito(venta: Venta, items: Map<Long, Int>): Result<Boolean, VentaError> {
        logger.debug { "crearLineasCarrito: $venta, $items" }
        items.forEach { item ->
            val producto = productosRepository.findById(item.key)
                ?: return Err(VentaError.VentaNoValidoError("Producto con id ${item.key} no es válido"))
            logger.debug { "Producto encontrado: $producto" }
            val linea = LineaVenta(
                ventaId = venta.id,
                lineaId = venta.nextLineaId,
                productoId = producto.id,
                cantidad = item.value,
                productoPrecio = producto.precio
            )
            venta.addLinea(linea)
        }
        return Ok(true)
    }

    private fun actualizarStock(items: Map<Long, Int>): Result<Boolean, VentaError> {
        items.forEach { item ->
            val producto = productosRepository.findById(item.key)
                ?: return Err(VentaError.VentaNoValidoError("Producto con id ${item.key} no es válido"))
            logger.debug { "Producto encontrado: $producto" }
            val updated = producto.copy(cantidad = producto.cantidad - item.value)
            productosRepository.update(updated)
        }
        return Ok(true)
    }

    private fun comprobarExistenciaProductos(items: Map<Long, Int>): Result<Boolean, VentaError> {
        logger.debug { "comprobarExistenciaProductos: $items" }
        items.forEach { item ->
            val producto = productosRepository.findById(item.key)
                ?: return Err(VentaError.VentaNoValidoError("Producto con id ${item.key} no es válido"))
            logger.debug { "Producto encontrado: $producto" }
            if (producto.cantidad < item.value) {
                // Esto si que es un error de negocio o excepción de dominio
                return Err(VentaError.VentaNoValidoError("Producto con id ${item.key} no tiene suficiente stock"))
            }
        }
        return Ok(true)
    }

    fun findById(id: Long): Result<Venta, VentaError> {
        logger.info { "findById: $id" }
        ventasRepository.findById(id)?.let {
            return Ok(it)
        } ?: return Err(VentaError.VentaNoExisteError("Venta con id $id no encontrada en almacenamiento"))
    }

    fun exportData() {
        logger.info { "Ventas export to Storage" }
        val ventas = ventasRepository.findAll()
        exportStorage.saveAll(ventas)
        logger.debug { "Number of Ventas exported to Storage: ${ventas.size}" }
    }

    fun exportInvoice(venta: Venta) {
        logger.info { "saveInvoice: $venta" }
        exportStorage.saveVenta(venta)
    }
}
