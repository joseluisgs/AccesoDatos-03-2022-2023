package repositories.raqueta

import entities.RaquetasDao
import entities.RaquetasTable
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import mappers.fromRaquetaDaoToRaqueta
import models.Raqueta
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

class RaquetasRepositoryImpl(
    private val raquetasDao: UUIDEntityClass<RaquetasDao>,
    // private val tenistaDao: UUIDEntityClass<TenistaDao>
) : RaquetasRepository {

    override suspend fun findAll(): Flow<Raqueta> = newSuspendedTransaction (Dispatchers.IO) {
        logger.debug { "findAll()" }
        raquetasDao.all()
            .map { it.fromRaquetaDaoToRaqueta() }
            .asFlow()
    }

    override suspend fun findById(id: UUID): Deferred<Raqueta?> = suspendedTransactionAsync(Dispatchers.IO) {
        logger.debug { "findById($id)" }
        raquetasDao.findById(id)
            ?.fromRaquetaDaoToRaqueta() //?: throw RaquetaException("Raqueta no encontrada con id: $id") // No es obligatorio el throw, porque devolvemos Raqueta? lo sería si es Raqueta
    }

    suspend fun findByMarca(marca: String): Flow<Raqueta> = newSuspendedTransaction(Dispatchers.IO) {
        logger.debug { "findByMarca($marca)" }
        raquetasDao.find { RaquetasTable.marca eq marca }
            .map { it.fromRaquetaDaoToRaqueta() }
            .asFlow()
    }

    override suspend fun save(entity: Raqueta): Deferred<Raqueta> = suspendedTransactionAsync(Dispatchers.IO) {
        // Existe?
        val existe = raquetasDao.findById(entity.uuid)

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


    override suspend fun delete(entity: Raqueta): Deferred<Boolean> = suspendedTransactionAsync(Dispatchers.IO) {
        // Existe?
        val existe = raquetasDao.findById(entity.uuid) ?: return@suspendedTransactionAsync false
        // Si existe lo borramos
        logger.debug { "delete($entity) - borrando" }
        existe.delete()
        return@suspendedTransactionAsync true
    }
}

