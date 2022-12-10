package repositories.raqueta

import entities.RaquetasTable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import mappers.toRaqueta
import mappers.toRaquetaEntity
import models.Raqueta
import mu.KotlinLogging
import org.ufoss.kotysa.R2dbcSqlClient
import java.util.*

private val logger = KotlinLogging.logger {}

class RaquetasRepositoryImpl(
    private val client: R2dbcSqlClient,
) : RaquetasRepository {

    override fun findAll(): Flow<Raqueta> {
        logger.debug { "findAll()" }
        return (client selectAllFrom RaquetasTable)
            .map { it.toRaqueta() }
    }

    override suspend fun findById(id: UUID): Raqueta? {
        logger.debug { "findById($id)" }
        return (client selectFrom RaquetasTable
                where RaquetasTable.uuid eq id
                ).fetchOneOrNull()?.toRaqueta()
    }

    override fun findByMarca(marca: String): Flow<Raqueta> {
        logger.debug { "findByMarca($marca)" }
        return (client selectFrom RaquetasTable
                where RaquetasTable.marca eq marca
                ).fetchAll()
            .map { it.toRaqueta() }

    }

    override suspend fun save(entity: Raqueta): Raqueta? {
        // Existe?
        val existe = findById(entity.uuid)
        // Esta alrternativa let/run es muy usada en Kotlin, como el if else...
        existe?.let {
            // Si existe actualizamos
            return update(entity)
        } ?: run {
            return insert(entity)
        }
    }

    private suspend fun insert(entity: Raqueta): Raqueta? = client.transactional {
        logger.debug { "save($entity) - creando" }
        client insert entity.toRaquetaEntity()
        return@transactional entity
    }

    private suspend fun update(entity: Raqueta): Raqueta? = client.transactional {
        logger.debug { "save($entity) - actualizando" }
        val raqueta = entity.toRaquetaEntity()
        (client update RaquetasTable
                set RaquetasTable.marca eq raqueta.marca
                set RaquetasTable.precio eq raqueta.precio
                where RaquetasTable.uuid eq raqueta.uuid)
            .execute()
        return@transactional entity
    }

    override suspend fun delete(entity: Raqueta): Raqueta? = client.transactional {
        // Existe?
        val existe = findById(entity.uuid)
        existe?.let {
            // Si existe actualizamos
            (client deleteFrom RaquetasTable where RaquetasTable.uuid eq entity.uuid)
                .execute()
            return@transactional entity
        }
    }
}

