package controllers

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.Flow
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
    // Puedo devolver el flujo
    suspend fun getTenistas(): Flow<Tenista> {
        logger.info("Obteniendo Tenistas")
        return tenistasRepository.findAll()
    }

    // Si hubiera hecho el await en el repositorio y devuelto el resultado directamente
    //Podría devolver el deferred y consumirlo más adelante
    // o devolver el resultado aquí ya
    // pero si estuviesemos en Ktor o donde sea de las rutas sería aquí..

    suspend fun createTenista(tenista: Tenista): Tenista {
        logger.debug { "Creando tenista $tenista" }
        return tenistasRepository.save(tenista).await()
    }

    suspend fun getTenistaById(uuid: UUID): Tenista? {
        logger.debug { "Obteniendo tenista con uuid $uuid" }
        return tenistasRepository.findById(uuid).await()
    }

    suspend fun updateTenista(tenista: Tenista) {
        logger.debug { "Updating tenista $tenista" }
        tenistasRepository.save(tenista).await()
    }

    suspend fun deleteTenista(it: Tenista): Boolean {
        logger.debug { "Borrando tenista $it" }
        return tenistasRepository.delete(it).await()
    }

    // RAQUETAS
    suspend fun getRaquetas(): Flow<Raqueta> {
        logger.info("Obteniendo Raquetas")
        return raquetasRepository.findAll()
    }

    suspend fun createRaqueta(raqueta: Raqueta): Raqueta {
        logger.debug { "Creando raqueta $raqueta" }
        return raquetasRepository.save(raqueta).await()
    }

    suspend fun getRaquetaById(uuid: UUID): Raqueta? {
        logger.debug { "Obteniendo raqueta con uuid $uuid" }
        return raquetasRepository.findById(uuid).await()
    }

    suspend fun updateRaqueta(raqueta: Raqueta): Raqueta {
        logger.debug { "Actualizando $raqueta" }
        return raquetasRepository.save(raqueta).await()
    }

    suspend fun deleteRaqueta(it: Raqueta): Boolean {
        logger.debug { "Borrando raqueta $it" }
        return raquetasRepository.delete(it).await()
    }
}