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

private val logger = KotlinLogging.logger {}

class MutuaController(
    private val raquetasRepository: RaquetasRepository,
    private val tenistasRepository: TenistasRepository
) {

    // TENISTAS

    fun getTenistas(): Flow<List<Tenista>> {
        logger.debug { "Obteniendo Tenistas" }
        return tenistasRepository.findAll()
            .flowOn(Dispatchers.IO)
    }

    suspend fun createTenista(tenista: Tenista): Tenista {
        logger.debug { "Creando tenista $tenista" }
        return tenistasRepository.save(tenista)
            ?: throw TenistaException("No se ha podido crear el tenista con uuid ${tenista.id}")
    }

    suspend fun getTenistaById(id: Long): Tenista {
        logger.debug { "Obteniendo tenista con uuid $id" }
        return tenistasRepository.findById(id)
            ?: throw TenistaException("No existe el tenista con uuid $id")
    }

    suspend fun updateTenista(tenista: Tenista): Tenista {
        logger.debug { "Updating tenista $tenista" }
        return tenistasRepository.save(tenista)
            ?: throw TenistaException("No se ha podido actualizar el tenista con uuid ${tenista.id}")

    }

    suspend fun deleteTenista(tenista: Tenista): Tenista {
        logger.debug { "Borrando tenista $tenista" }
        return tenistasRepository.delete(tenista)
            ?: throw TenistaException("No se ha podido borrar el tenista con uuid ${tenista.id}")
    }

    // RAQUETAS
    fun getRaquetas(): Flow<List<Raqueta>> {
        logger.info("Obteniendo Raquetas")
        return raquetasRepository.findAll()
            .flowOn(Dispatchers.IO)
    }

    suspend fun createRaqueta(raqueta: Raqueta): Raqueta {
        logger.debug { "Creando raqueta $raqueta" }
        return raquetasRepository.save(raqueta)
            ?: throw RaquetaException("No se ha podido crear la raqueta con uuid ${raqueta.id}")
    }

    suspend fun getRaquetaById(id: Long): Raqueta {
        logger.debug { "Obteniendo raqueta con uuid $id" }
        return raquetasRepository.findById(id)
            ?: throw RaquetaException("No existe la raqueta con uuid $id")
    }

    suspend fun updateRaqueta(raqueta: Raqueta): Raqueta {
        logger.debug { "Actualizando $raqueta" }
        return raquetasRepository.save(raqueta)
            ?: throw RaquetaException("No se ha podido actualizar la raqueta con uuid ${raqueta.id}")

    }

    suspend fun deleteRaqueta(raqueta: Raqueta): Raqueta {
        logger.debug { "Borrando raqueta $raqueta" }
        return raquetasRepository.delete(raqueta)
            ?: throw RaquetaException("No existe la raqueta con uuid ${raqueta.id}")
    }
}