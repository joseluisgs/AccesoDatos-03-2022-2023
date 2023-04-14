package controllers

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.andThen
import errors.ProductoError
import models.Producto
import repositories.productos.ProductosRepository
import validators.validar

// Usamos un controlador o servicio para manejar la lógica de negocio, filtrar excepciones, filtrar datos, control nulos, validar, etc.

private val logger = mu.KotlinLogging.logger {}


class ProductosController(
    private val productosRepository: ProductosRepository,
) {

    fun findAll(): List<Producto> {
        logger.info { "findAll" }
        return productosRepository.findAll()
    }

    fun findAllDisponible(disponible: Boolean = true): List<Producto> {
        logger.info { "findAll: $disponible" }
        return productosRepository.findAll().filter { it.disponible == disponible }
    }

    fun findById(id: Long): Result<Producto, ProductoError> {
        logger.info { "findById: $id" }
        return productosRepository.findById(id)?.let {
            Ok(it)
        } ?: Err(ProductoError.NoEncontrado("Producto con $id no existe en almacenamiento"))
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
        // Lo buscamos
        return findById(producto.id)
            // lo validamos
            .andThen { it.validar() }
            // lo actualizamos
            .andThen { Ok(productosRepository.update(it)) }
    }

    fun deleteById(id: Long): Result<Boolean, ProductoError> {
        logger.info { "deleteById: $id" }
        // Lo buscamos
        return findById(id)
            // lo validamos
            .andThen { it.validar() }
            // lo eliminamos
            .andThen { Ok(productosRepository.deleteById(id)) }
    }
}