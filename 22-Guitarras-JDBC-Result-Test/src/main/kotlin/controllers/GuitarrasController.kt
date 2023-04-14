package controllers

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.andThen
import errors.GuitarraError
import models.Guitarra
import mu.KotlinLogging
import repositories.guitarras.GuitarrasRepository
import services.guitarras.GuitarrasStorage
import validators.validate
import java.util.*

private val logger = KotlinLogging.logger {}

class GuitarrasController(
    private val guitarrasRepository: GuitarrasRepository,
    private val guitarrasStorage: GuitarrasStorage
) {

    fun getAll(): List<Guitarra> {
        logger.info { "Obteniendo todas las guitarras" }

        return guitarrasRepository.findAll().toList()
    }

    fun getById(id: Long): Result<Guitarra, GuitarraError> {
        logger.info { "Obteniendo la guitarra con id: $id" }

        guitarrasRepository.findById(id).let {
            return if (it != null) {
                Ok(it)
            } else {
                Err(GuitarraError.GuitarraNoEncontrada("No se ha encontrado la guitarra con id: $id"))
            }
        }
    }

    fun getByUuid(uuid: UUID): Result<Guitarra, GuitarraError> {
        logger.info { "Obteniendo la guitarra con uuid: $uuid" }

        guitarrasRepository.findByUuid(uuid).let {
            return if (it != null) {
                Ok(it)
            } else {
                Err(GuitarraError.GuitarraNoEncontrada("No se ha encontrado la guitarra con uuid: $uuid"))
            }
        }
    }

    fun getByMarca(marca: String): List<Guitarra> {
        logger.info { "Obteniendo la guitarra con marca: $marca" }

        return guitarrasRepository.findByMarca(marca).toList()
    }

    fun save(guitarra: Guitarra): Result<Guitarra, GuitarraError> {
        logger.info { "Guardando la guitarra: $guitarra" }

        return guitarra.validate().andThen {
            Ok(guitarrasRepository.save(it))
        }
    }

    fun delete(guitarra: Guitarra): Result<Boolean, GuitarraError> {
        logger.info { "Eliminando la guitarra: $guitarra" }

        guitarrasRepository.delete(guitarra).let {
            return if (it) {
                Ok(true)
            } else {
                Err(GuitarraError.GuitarraNoEncontrada("No se ha encontrado la guitarra con id: ${guitarra.id}"))
            }
        }
    }

    fun delete(id: Long): Result<Boolean, GuitarraError> {
        logger.info { "Eliminando la guitarra con id: $id" }

        guitarrasRepository.deleteById(id).let {
            return if (it) {
                Ok(true)
            } else {
                Err(GuitarraError.GuitarraNoEncontrada("No se ha encontrado la guitarra con id: $id"))
            }
        }
    }

    private fun deleteAll() {
        logger.info { "Eliminando todas las guitarras" }

        guitarrasRepository.deleteAll()
    }

    fun importData(deleteAll: Boolean = false) {
        if (deleteAll) {
            deleteAll()
        }

        guitarrasStorage.loadData().forEach {
            guitarrasRepository.save(it)
        }
    }

    fun exportData() {
        guitarrasStorage.saveData(guitarrasRepository.findAll().toList())
    }
}