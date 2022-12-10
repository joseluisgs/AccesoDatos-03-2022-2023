package es.joseluisgs.p06encordadosspringdatakotlin.controllers

import es.joseluisgs.p06encordadosspringdatakotlin.exceptions.RaquetaException
import es.joseluisgs.p06encordadosspringdatakotlin.models.Raqueta
import es.joseluisgs.p06encordadosspringdatakotlin.models.Tenista
import es.joseluisgs.p06encordadosspringdatakotlin.repositories.raqueta.RaquetasRepository
import es.joseluisgs.p06encordadosspringdatakotlin.repositories.tenista.TenistasRepository
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import java.util.*

val logger = KotlinLogging.logger {}

// Le inyectamos las dependencias
@Controller
class MutuaController
@Autowired constructor(
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

    fun getTenistaById(uuid: UUID): Tenista {
        logger.debug { "Obteniendo tenista con uuid $uuid" }
        return tenistasRepository.findById(uuid).orElse(null)
    }

    fun updateTenista(tenista: Tenista) {
        logger.debug { "Updating tenista $tenista" }
        tenistasRepository.save(tenista)
    }

    fun deleteTenista(it: Tenista): Boolean {
        logger.debug { "Borrando tenista $it" }
        tenistasRepository.delete(it)
        return true
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
        return raquetasRepository.findById(uuid).orElse(null)
    }

    fun updateRaqueta(raqueta: Raqueta): Raqueta {
        logger.debug { "Actualizando $raqueta" }
        return raquetasRepository.save(raqueta)
    }

    fun deleteRaqueta(it: Raqueta): Boolean {
        logger.debug { "Borrando raqueta $it" }
        raquetasRepository.delete(it)
        return true
    }

    fun findRaquetaByMarca(marca: String): Raqueta {
        logger.debug { "Buscando raqueta por marca $marca" }
        return raquetasRepository.findByMarca(marca)
            ?: throw RaquetaException("No se ha encontrado la raqueta con marca $marca")
    }
}