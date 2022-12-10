package repositories.raqueta

import entities.RaquetasDao
import entities.RaquetasTable
import mappers.fromRaquetaDaoToRaqueta
import models.Raqueta
import mu.KotlinLogging
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

private val logger = KotlinLogging.logger {}

class RaquetasRepositoryImpl(
    private val raquetasDao: UUIDEntityClass<RaquetasDao>,
    // private val tenistaDao: UUIDEntityClass<TenistaDao>
) : RaquetasRepository {

    override fun findAll(): List<Raqueta> = transaction {
        logger.debug { "findAll()" }
        raquetasDao.all().map { it.fromRaquetaDaoToRaqueta() }
    }

    override fun findById(id: UUID): Raqueta? = transaction {
        logger.debug { "findById($id)" }
        raquetasDao.findById(id)
            ?.fromRaquetaDaoToRaqueta() //?: throw RaquetaException("Raqueta no encontrada con id: $id") // No es obligatorio el throw, porque devolvemos Raqueta? lo sería si es Raqueta
    }

    fun findByMarca(marca: String): List<Raqueta> = transaction {
        logger.debug { "findByMarca($marca)" }
        raquetasDao.find { RaquetasTable.marca eq marca }.map { it.fromRaquetaDaoToRaqueta() }
    }

    override fun save(entity: Raqueta): Raqueta = transaction {
        // Existe?
        val existe = raquetasDao.findById(entity.uuid)
        // Esta alrternativa let/run es muy usada en Kotlin, como el if else...

        existe?.let {
            // Si existe actualizamos
            update(entity, existe)
        } ?: run {
            insert(entity)
        }
    }

    private fun insert(entity: Raqueta): Raqueta {
        logger.debug { "save($entity) - creando" }
        // No existe, lo guardamos, le pongo el ID que he generado en el DATA val, si no podría haber usado el suyo sin paranetesis
        return raquetasDao.new(entity.uuid) {
            marca = entity.marca
            precio = entity.precio
            /*tenistas = SizedCollection(entity.tenistas.map {
                    tenistaDao.findById(it.id) ?: throw TenistaException("Tenista no encontrado con id: ${it.id}")
                })*/
        }.fromRaquetaDaoToRaqueta()
    }

    private fun update(entity: Raqueta, existe: RaquetasDao): Raqueta {
        logger.debug { "save($entity) - actualizando" }
        return existe.apply {
            marca = entity.marca
            precio = entity.precio
            /*tenistas = SizedCollection(entity.tenistas.map {
                    tenistaDao.findById(it.id) ?: throw TenistaException("Tenista no encontrado con id: ${it.id}")
                })*/
        }.fromRaquetaDaoToRaqueta()
    }


    override fun delete(entity: Raqueta): Boolean = transaction {
        // Existe?
        val existe = raquetasDao.findById(entity.uuid) ?: return@transaction false
        // Si existe lo borramos
        logger.debug { "delete($entity) - borrando" }
        existe.delete()
        true
    }
}

