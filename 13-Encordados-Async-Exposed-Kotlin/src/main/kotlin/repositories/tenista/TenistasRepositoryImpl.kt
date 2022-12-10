package repositories.tenista

import exceptions.RaquetaException
import entities.RaquetasDao
import entities.TenistasDao
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import mappers.fromTenistaDaoToTenista
import models.Tenista
import mu.KotlinLogging
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.experimental.suspendedTransactionAsync
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

private val logger = KotlinLogging.logger {}

// Vamos a tener en cuenta esto
// https://github.com/JetBrains/Exposed/wiki/Transactions#working-with-coroutines
// podríamos obetner el resultado asíncrono y devolver deferred para consumirlo
// más adelante o  devolver flows
// Vamos a hacer una mezcla de ambas cosas :)
// También podemos hacer el asyn y await aquí y devolver el resultado directamente
// dentro del suspendTransaction
// Tú eliges el newSuspendedTransaction o el suspendedTransactionAsync

class TenistasRepositoryImpl(
    private val tenistasDao: UUIDEntityClass<TenistasDao>,
    private val raquetasDao: UUIDEntityClass<RaquetasDao>
) : TenistasRepository {

    override suspend fun findAll(): Flow<Tenista> =  newSuspendedTransaction(Dispatchers.IO) {
        logger.debug { "findAll()" }
        tenistasDao.all()
            .map { it.fromTenistaDaoToTenista() }
            .asFlow()
    }

    override suspend fun findById(id: UUID): Deferred<Tenista?> =  suspendedTransactionAsync(Dispatchers.IO) {
        logger.debug { "findById($id)" }
        tenistasDao.findById(id)?.fromTenistaDaoToTenista()
    }

    override suspend fun save(entity: Tenista): Deferred<Tenista> =  suspendedTransactionAsync(Dispatchers.IO) {
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

    override suspend fun delete(entity: Tenista): Deferred<Boolean> =  suspendedTransactionAsync(Dispatchers.IO) {
        val existe = tenistasDao.findById(entity.uuid) ?: return@suspendedTransactionAsync false
        // Si existe lo borramos
        logger.debug { "delete($entity) - borrando" }
        existe.delete()
        return@suspendedTransactionAsync true
    }
}

