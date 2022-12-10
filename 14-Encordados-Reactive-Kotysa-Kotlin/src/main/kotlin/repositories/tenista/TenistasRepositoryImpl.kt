package repositories.tenista

import entities.RaquetasTable
import entities.TenistasTable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import mappers.toRaqueta
import mappers.toTenista
import mappers.toTenistaEntity
import models.Raqueta
import models.Tenista
import mu.KotlinLogging
import org.ufoss.kotysa.R2dbcSqlClient
import java.util.*

private val logger = KotlinLogging.logger {}

class TenistasRepositoryImpl(
    private val client: R2dbcSqlClient,
) : TenistasRepository {
    override fun findAll(): Flow<Tenista> {
        logger.debug { "findAll()" }
        return (client selectAllFrom TenistasTable)
            // Join with Raqueta para asignarla al objeto del modelo, es un if!=null
            .map {
                it.raquetaUuid?.let { raquetaUuid ->
                    it.toTenista(findRaquetaById(raquetaUuid))
                } ?: it.toTenista(null)
            }
    }

    override suspend fun findById(id: UUID): Tenista? {
        logger.debug { "findById($id)" }

        val raquetaEntity = (client selectFrom TenistasTable
                where TenistasTable.uuid eq id
                ).fetchOneOrNull()
        // Join with Raqueta para asignarla al objeto del modelo, es un if!=null
        return raquetaEntity?.let {
            it.raquetaUuid?.let { raquetaUuid ->
                it.toTenista(findRaquetaById(raquetaUuid))
            } ?: it.toTenista(null)
        }
    }

    override suspend fun save(entity: Tenista): Tenista? {
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

    private suspend fun insert(entity: Tenista): Tenista? = client.transactional {
        logger.debug { "save($entity) - creando" }
        client insert entity.toTenistaEntity()
        return@transactional entity
    }

    private suspend fun update(entity: Tenista): Tenista? = client.transactional {
        logger.debug { "save($entity) - actualizando" }
        val tenista = entity.toTenistaEntity()
        (client update TenistasTable
                set TenistasTable.nombre eq tenista.nombre
                set TenistasTable.ranking eq tenista.ranking
                set TenistasTable.ganancias eq tenista.ganancias
                // así podríamos seguir con todos los campos que queramos
                set TenistasTable.raquetaUuid eq tenista.raquetaUuid
                where TenistasTable.uuid eq tenista.uuid)
            .execute()
        return@transactional entity
    }

    override suspend fun delete(entity: Tenista): Tenista? = client.transactional {
        // Existe?
        val existe = findById(entity.uuid)
        existe.let {
            // Si existe actualizamos
            (client deleteFrom TenistasTable where TenistasTable.uuid eq entity.uuid)
                .execute()
            return@transactional entity
        }
    }

    private suspend fun findRaquetaById(id: UUID): Raqueta? {
        logger.debug { "findRaquetaById($id)" }

        return (client selectFrom RaquetasTable
                where RaquetasTable.uuid eq id
                ).fetchOneOrNull()?.toRaqueta()
    }
}

