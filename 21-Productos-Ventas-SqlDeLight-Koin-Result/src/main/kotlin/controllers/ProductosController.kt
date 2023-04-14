package controllers

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.andThen
import errors.ProductoError
import models.Producto
import mu.KotlinLogging
import org.koin.core.annotation.Named
import org.koin.core.annotation.Singleton
import repositories.productos.ProductosRepository
import services.storage.productos.ProductosStorageService
import validators.validar

private val logger = KotlinLogging.logger {}

@Singleton
class ProductosController(
    private val productosRepository: ProductosRepository,
    @Named("ProductosCsvService") // Usamos el nombre para diferenciar los servicios, al pertenecer a la misma interfaz
    private val importStorage: ProductosStorageService,
    @Named("ProductosJsonService") // Usamos el nombre para diferenciar los servicios, al pertenecer a la misma interfaz
    private val exportStorage: ProductosStorageService
) {

    fun findAll(): List<Producto> {
        logger.info { "findAll" }
        return productosRepository.findAll()
    }

    fun findAllByDisponible(disponible: Boolean = true): List<Producto> {
        logger.info { "findAll: $disponible" }
        return productosRepository.findAllByDisponible(disponible)
    }

    fun findById(id: Long): Result<Producto, ProductoError> {
        logger.info { "findById: $id" }
        productosRepository.findById(id)?.let {
            return Ok(it)
        } ?: return Err(ProductoError.ProductoNoEncontradoError("Producto con id $id no existe en almacenamiento"))

    }

    fun findByNombre(nombre: String): List<Producto> {
        logger.info { "findByNombre: $nombre" }
        return productosRepository.findByNombre(nombre)
    }

    fun save(producto: Producto): Result<Producto, ProductoError> {
        logger.info { "save: $producto" }
        return producto
            .validar()
            .andThen { Ok(productosRepository.save(it)) }
    }

    fun update(producto: Producto): Result<Producto, ProductoError> {
        logger.info { "update: $producto" }
        return findById(producto.id)
            .andThen { it.validar() }
            .andThen { Ok(productosRepository.update(it)) }
    }

    fun deleteById(id: Long): Result<Boolean, ProductoError> {
        logger.info { "deleteById: $id" }
        return findById(id)
            .andThen { Ok(productosRepository.deleteById(it.id)) }
    }

    fun exportData() {
        logger.info { "Productos export to Storage" }
        val productos = productosRepository.findAll()
        exportStorage.saveAll(productos)
        logger.debug { "Number of Productos exported to Storage: ${productos.size}" }
    }

    fun importData() {
        logger.info { "Productos import from Storage" }
        val productos = importStorage.loadAll()
        logger.debug { "Number of Productos imported from Storage: ${productos.size}" }
        // borramos antes todos los productos
        // productosRepository.deleteAll() // Me borra los datos que tengo en la base de datos pro script
        // los recorremos e insertamos en el almacén
        productos.forEach { producto ->
            productosRepository.save(producto)
        }
    }
}