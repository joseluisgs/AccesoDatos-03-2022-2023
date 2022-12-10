package controller

import model.Tenista
import mu.KotlinLogging
import repository.TenistasRepository
import java.util.*

private val logger = KotlinLogging.logger {}

class MutuaController(private val tenistasRepository: TenistasRepository) {

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


}
