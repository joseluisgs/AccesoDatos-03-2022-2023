package controllers

import exceptions.RaquetaException
import exceptions.TenistaException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import models.Raqueta
import models.Tenista
import mu.KotlinLogging
import repositories.raqueta.RaquetasRepository
import repositories.tenista.TenistasRepository
import java.util.*

private val logger = KotlinLogging.logger {}

class MutuaController(
    private val raquetasRepository: RaquetasRepository,
    private val tenistasRepository: TenistasRepository
) {

    // TENISTAS

    fun getTenistas(): Flow<Tenista> {
        logger.debug { "Obteniendo Tenistas" }
        return tenistasRepository.findAll()
            .flowOn(Dispatchers.IO)
    }

    suspend fun createTenista(tenista: Tenista): Tenista {
        logger.debug { "Creando tenista $tenista" }
        return tenistasRepository.save(tenista)
            ?: throw TenistaException("No se ha podido crear el tenista con uuid ${tenista.uuid}")
    }

    suspend fun getTenistaById(uuid: UUID): Tenista {
        logger.debug { "Obteniendo tenista con uuid $uuid" }
        return tenistasRepository.findById(uuid)
            ?: throw TenistaException("No existe el tenista con uuid $uuid")
    }

    suspend fun updateTenista(tenista: Tenista): Tenista {
        logger.debug { "Updating tenista $tenista" }
        return tenistasRepository.save(tenista)
            ?: throw TenistaException("No se ha podido actualizar el tenista con uuid ${tenista.uuid}")

    }

    suspend fun deleteTenista(tenista: Tenista): Tenista {
        logger.debug { "Borrando tenista $tenista" }
        return tenistasRepository.delete(tenista)
            ?: throw TenistaException("No se ha podido borrar el tenista con uuid ${tenista.uuid}")
    }

    // RAQUETAS
    fun getRaquetas(): Flow<Raqueta> {
        logger.info("Obteniendo Raquetas")
        return raquetasRepository.findAll()
            .flowOn(Dispatchers.IO)
    }

    suspend fun createRaqueta(raqueta: Raqueta): Raqueta {
        logger.debug { "Creando raqueta $raqueta" }
        return raquetasRepository.save(raqueta)
            ?: throw RaquetaException("No se ha podido crear la raqueta con uuid ${raqueta.uuid}")
    }

    suspend fun getRaquetaById(uuid: UUID): Raqueta {
        logger.debug { "Obteniendo raqueta con uuid $uuid" }
        return raquetasRepository.findById(uuid)
            ?: throw RaquetaException("No existe la raqueta con uuid $uuid")
    }

    suspend fun updateRaqueta(raqueta: Raqueta): Raqueta {
        logger.debug { "Actualizando $raqueta" }
        return raquetasRepository.save(raqueta)
            ?: throw RaquetaException("No se ha podido actualizar la raqueta con uuid ${raqueta.uuid}")

    }

    suspend fun deleteRaqueta(raqueta: Raqueta): Raqueta {
        logger.debug { "Borrando raqueta $raqueta" }
        return raquetasRepository.delete(raqueta)
            ?: throw RaquetaException("No existe la raqueta con uuid ${raqueta.uuid}")
    }
}