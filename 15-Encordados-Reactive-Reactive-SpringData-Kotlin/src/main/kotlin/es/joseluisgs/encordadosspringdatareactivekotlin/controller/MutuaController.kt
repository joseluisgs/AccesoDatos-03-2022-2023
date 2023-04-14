package es.joseluisgs.encordadosspringdatareactivekotlin.controller

import es.joseluisgs.encordadosspringdatareactivekotlin.models.Raqueta
import es.joseluisgs.encordadosspringdatareactivekotlin.repositories.raqueta.RaquetasRepository
import es.joseluisgs.encordadosspringdatareactivekotlin.repositories.tenista.TenistasRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import mappers.toRaqueta
import mappers.toRaquetaEntity
import mappers.toTenista
import mappers.toTenistaEntity
import models.Tenista
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller

private val logger = KotlinLogging.logger {}

@Controller
class MutuaController
@Autowired constructor(
    private val raquetasRepository: RaquetasRepository,
    private val tenistasRepository: TenistasRepository
) {

    // TENISTAS

    suspend fun getTenistas(): Flow<Tenista> {
        logger.debug { "Obteniendo Tenistas" }
        // Para estas cosas lo mejor es montarse un servicio y que el controlador solo llame a los métodos
        return tenistasRepository.findAll().map { it ->
            it.toTenista(
                it.raquetaId?.let { id -> raquetasRepository.findById(id)?.toRaqueta() }
            )
        }
    }

    suspend fun createTenista(tenista: Tenista): Tenista {
        logger.debug { "Creando tenista $tenista" }
        return tenistasRepository.save(tenista.toTenistaEntity())
            .toTenista(tenista.raqueta?.id?.let { raquetasRepository.findById(it)?.toRaqueta() })

    }

    suspend fun getTenistaById(id: Long): Tenista {
        logger.debug { "Obteniendo tenista con uuid $id" }
        val tenistaEntity = tenistasRepository.findById(id)
            ?: throw Exception("No se ha encontrado el tenista con uuid $id")
        return tenistaEntity.toTenista(
            tenistaEntity.raquetaId?.let { raquetasRepository.findById(it)?.toRaqueta() }
        )

    }

    suspend fun updateTenista(tenista: Tenista): Tenista {
        logger.debug { "Updating tenista $tenista" }
        return tenistasRepository.save(tenista.toTenistaEntity())
            .toTenista(tenista.raqueta?.id?.let { raquetasRepository.findById(it)?.toRaqueta() })
    }

    suspend fun deleteTenista(tenista: Tenista): Boolean {
        logger.debug { "Borrando tenista $tenista" }
        tenistasRepository.delete(tenista.toTenistaEntity())
        return true
    }

    // RAQUETAS
    fun getRaquetas(): Flow<Raqueta> {
        logger.info("Obteniendo Raquetas")
        return raquetasRepository
            .findAll()
            .map { it.toRaqueta() }
    }

    suspend fun createRaqueta(raqueta: Raqueta): Raqueta {
        logger.debug { "Creando raqueta $raqueta" }
        return raquetasRepository
            .save(raqueta.toRaquetaEntity())
            .toRaqueta()
    }

    suspend fun getRaquetaById(id: Long): Raqueta? {
        logger.debug { "Obteniendo raqueta con uuid $id" }
        return raquetasRepository
            .findById(id)?.toRaqueta()
    }

    suspend fun updateRaqueta(raqueta: Raqueta): Raqueta {
        logger.debug { "Actualizando $raqueta" }
        return raquetasRepository
            .save(raqueta.toRaquetaEntity())
            .toRaqueta()
    }

    suspend fun deleteRaqueta(raqueta: Raqueta): Boolean {
        logger.debug { "Borrando raqueta $raqueta" }
        raquetasRepository.delete(raqueta.toRaquetaEntity())
        return true
    }

    fun findRaquetaByMarca(marca: String): Flow<Raqueta> {
        logger.debug { "Buscando raqueta por marca $marca" }
        return raquetasRepository
            .findByMarca(marca)
            .map { it.toRaqueta() }
    }
}