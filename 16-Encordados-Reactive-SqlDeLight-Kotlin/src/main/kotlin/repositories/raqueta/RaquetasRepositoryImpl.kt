package repositories.raqueta

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import db.SqlDeLightClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import mappers.toRaqueta
import models.Raqueta
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

class RaquetasRepositoryImpl(
    private val client: SqlDeLightClient,
) : RaquetasRepository {

    override fun findAll(): Flow<List<Raqueta>> {
        logger.debug { "findAll()" }
        return client.queries.selectAllRaquetas()
            .asFlow()
            .mapToList() // tenemos RaquetaEntity
            .map { raquetaListEntity -> raquetaListEntity.map { it.toRaqueta() } }

    }

    override suspend fun findById(id: Long): Raqueta? {
        logger.debug { "findById($id)" }
        return client.queries.selectRaquetaById(id)
            .executeAsOneOrNull()
            ?.toRaqueta()
    }

    override fun findByMarca(marca: String): Flow<List<Raqueta>> {
        logger.debug { "findByMarca($marca)" }
        return client.queries.selectRaquetaByMarca(marca)
            .asFlow()
            .mapToList()
            .map { raquetaEntity -> raquetaEntity.map { it.toRaqueta() } }

    }

    override suspend fun save(entity: Raqueta): Raqueta? {
        logger.debug { "save($entity)" }
        // Primero comprobamos si existe
        val raqueta = findById(entity.id)
        raqueta?.let {
            return update(entity)
        } ?: run {
            return insert(entity)
        }
    }

    private suspend fun insert(entity: Raqueta): Raqueta? {
        logger.debug { "save($entity) - creando" }
        client.queries.insertRaqueta(id = null, marca = entity.marca, precio = entity.precio)
        // ahora obtenemos el ultimo
        return client.queries.selectLastRaqueta()
            .executeAsOneOrNull()
            ?.toRaqueta()
    }

    private suspend fun update(entity: Raqueta): Raqueta {
        logger.debug { "save($entity) - actualizando" }
        client.queries.updateRaqueta(entity.marca, entity.precio, entity.id)
        return entity
    }

    override suspend fun delete(entity: Raqueta): Raqueta? {
        logger.debug { "delete($entity) - eliminando" }
        findById(entity.id)?.let {
            client.queries.deleteRaquetaById(entity.id)
            return entity
        }
        return null
    }
}

