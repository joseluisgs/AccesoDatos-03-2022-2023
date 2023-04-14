package repositories.tenista

import exceptions.RaquetaException
import entities.RaquetasDao
import entities.TenistasDao
import mappers.fromTenistaDaoToTenista
import models.Tenista
import mu.KotlinLogging
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

private val logger = KotlinLogging.logger {}

class TenistasRepositoryImpl(
    private val tenistasDao: UUIDEntityClass<TenistasDao>,
    private val raquetasDao: UUIDEntityClass<RaquetasDao>
) : TenistasRepository {
    override fun findAll(): List<Tenista> = transaction {
        logger.debug { "findAll()" }
        tenistasDao.all().map { it.fromTenistaDaoToTenista() }
    }

    override fun findById(id: UUID): Tenista? = transaction {
        logger.debug { "findById($id)" }
        tenistasDao.findById(id)?.fromTenistaDaoToTenista()
    }

    override fun save(entity: Tenista): Tenista = transaction {
        // Existe?
        val existe = tenistasDao.findById(entity.uuid)
        // Esta alrternativa let/run es muy usada en Kotlin, como el if else...
        existe?.let {
            update(entity, existe)
        } ?: run {
            insert(entity)
        }
    }

    private fun insert(entity: Tenista): Tenista {
        logger.debug { "save($entity) - creando" }
        // No existe, lo guardamos, le pongo el ID que he generado en el DATA val, si no podría haber usado el suyo sin paranetesis
        return tenistasDao.new(entity.uuid) {
            nombre = entity.nombre
            ranking = entity.ranking
            fechaNacimiento = entity.fechaNacimiento
            añoProfesional = entity.añoProfesional
            altura = entity.altura
            peso = entity.peso
            ganancias = entity.ganancias
            manoDominante = entity.manoDominante.mano
            tipoReves = entity.tipoReves.tipo
            puntos = entity.puntos
            pais = entity.pais
            raqueta = raquetasDao.findById(entity.raqueta!!.uuid)
                ?: throw RaquetaException("La raqueta no existe con id: $id")
        }.fromTenistaDaoToTenista()
    }

    private fun update(entity: Tenista, existe: TenistasDao): Tenista {
        logger.debug { "save($entity) - actualizando" }
        // Si existe actualizamos
        return existe.apply {
            nombre = entity.nombre
            ranking = entity.ranking
            fechaNacimiento = entity.fechaNacimiento
            añoProfesional = entity.añoProfesional
            altura = entity.altura
            peso = entity.peso
            ganancias = entity.ganancias
            manoDominante = entity.manoDominante.mano
            tipoReves = entity.tipoReves.tipo
            puntos = entity.puntos
            pais = entity.pais
            raqueta = raquetasDao.findById(entity.raqueta!!.uuid)
                ?: throw RaquetaException("La raqueta no existe con id: $id")
        }.fromTenistaDaoToTenista()
    }

    override fun delete(entity: Tenista): Boolean = transaction {
        val existe = tenistasDao.findById(entity.uuid) ?: return@transaction false
        // Si existe lo borramos
        logger.debug { "delete($entity) - borrando" }
        existe.delete()
        true
    }
}

