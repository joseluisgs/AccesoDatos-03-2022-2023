package controller

import model.Raqueta
import model.Tenista
import mu.KotlinLogging
import repository.raqueta.RaquetasRepository
import repository.tenista.TenistasRepository
import java.util.*

private val logger = KotlinLogging.logger {}

class MutuaController(
    private val raquetasRepository: RaquetasRepository,
    private val tenistasRepository: TenistasRepository
) {

    // TENISTAS
    fun getTenistas(): List<Tenista> {
        logger.info("Obteniendo Tenistas")
        return tenistasRepository.findAll()
    }

    fun createTenista(tenista: Tenista): Tenista {
        logger.debug { "Creando tenista $tenista" }
        tenistasRepository.save(tenista)
        return tenista
    }

    fun getTenistaById(uuid: UUID): Tenista? {
        logger.debug { "Obteniendo tenista con uuid $uuid" }
        return tenistasRepository.findById(uuid)
    }

    fun updateTenista(tenista: Tenista) {
        logger.debug { "Actualizando tenista $tenista" }
        tenistasRepository.save(tenista)
    }

    fun deleteTenista(it: Tenista): Boolean {
        logger.debug { "Borrando tenista $it" }
        return tenistasRepository.delete(it)
    }

    // RAQUETAS
    fun getRaquetas(): List<Raqueta> {
        logger.info("Obteniendo Raquetas")
        return raquetasRepository.findAll()
    }

    fun createRaqueta(raqueta: Raqueta): Raqueta {
        logger.debug { "Creando raqueta $raqueta" }
        raquetasRepository.save(raqueta)
        return raqueta
    }

    fun getRaquetaById(uuid: UUID): Raqueta? {
        logger.debug { "Obteniendo raqueta con uuid $uuid" }
        return raquetasRepository.findById(uuid)
    }

    fun updateRaqueta(raqueta: Raqueta): Raqueta {
        logger.debug { "Actualizando $raqueta" }
        return raquetasRepository.save(raqueta)
    }

    fun deleteRaqueta(raqueta: Raqueta): Boolean {
        logger.debug { "Borrando raqueta $raqueta" }
        return raquetasRepository.delete(raqueta)

    }


}
