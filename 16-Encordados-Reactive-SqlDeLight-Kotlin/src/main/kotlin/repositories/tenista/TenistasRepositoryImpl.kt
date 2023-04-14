package repositories.tenista

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import db.SqlDeLightClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import mappers.toRaqueta
import mappers.toTenista
import models.Raqueta
import models.Tenista
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

class TenistasRepositoryImpl(
    private val client: SqlDeLightClient,
) : TenistasRepository {
    override fun findAll(): Flow<List<Tenista>> {
        logger.debug { "findAll()" }
        return client.queries.selectAllTenistas().asFlow().mapToList() //TenistaEntity
            .map { tenistaListEntity ->
                tenistaListEntity.map {
                    // encontramos su raqueta
                    val raqueta = findRaquetaById(it.raqueta_id)
                    it.toTenista(raqueta)
                }
            }
    }

    override suspend fun findById(id: Long): Tenista? {
        logger.debug { "findById($id)" }
        val tenistaEntity = client.queries.selectTenistaById(id).executeAsOneOrNull()
        return tenistaEntity?.let {
            val raqueta = findRaquetaById(it.raqueta_id)
            return it.toTenista(raqueta)
        }
    }

    override suspend fun save(entity: Tenista): Tenista? {
        // existe
        val tenista = findById(entity.id)
        tenista?.let {
            return update(entity)
        } ?: run {
            return insert(entity)
        }
    }

    private suspend fun insert(entity: Tenista): Tenista? {
        logger.debug { "save($entity) - creando" }
        client.queries.insertTenista(
            id = null,
            nombre = entity.nombre,
            ranking = entity.ranking.toLong(),
            fecha_nacimiento = entity.fechaNacimiento.toString(),
            anno_profesional = entity.añoProfesional.toLong(),
            altura = entity.altura.toLong(),
            peso = entity.peso.toLong(),
            ganancias = entity.ganancias,
            mano_dominante = entity.manoDominante.mano,
            tipo_reves = entity.tipoReves.tipo,
            puntos = entity.puntos.toLong(),
            pais = entity.pais,
            raqueta_id = if (entity.raqueta != null) entity.raqueta!!.id else null
        )
        // Obtenemos el ultimo
        val tenistaEntity = client.queries.selectLastTenista().executeAsOneOrNull()
        return tenistaEntity?.let {
            val raqueta = findRaquetaById(it.raqueta_id)
            return it.toTenista(raqueta)
        }
    }

    private suspend fun update(entity: Tenista): Tenista {
        logger.debug { "save($entity) - actualizando" }
        client.queries.updateTenista(
            id = entity.id,
            nombre = entity.nombre,
            ranking = entity.ranking.toLong(),
            fecha_nacimiento = entity.fechaNacimiento.toString(),
            anno_profesional = entity.añoProfesional.toLong(),
            altura = entity.altura.toLong(),
            peso = entity.peso.toLong(),
            ganancias = entity.ganancias,
            mano_dominante = entity.manoDominante.mano,
            tipo_reves = entity.tipoReves.tipo,
            puntos = entity.puntos.toLong(),
            pais = entity.pais,
            raqueta_id = if (entity.raqueta != null) entity.raqueta!!.id else null
        )
        return entity
    }

    override suspend fun delete(entity: Tenista): Tenista? {
        logger.debug { "delete($entity) - eliminando" }
        findById(entity.id)?.let {
            client.queries.deleteTenistaById(entity.id)
            return entity
        }
        return null
    }

    private suspend fun findRaquetaById(id: Long?): Raqueta? {
        logger.debug { "findRaquetaById($id)" }
        return id?.let {
            client.queries.selectRaquetaById(id)
                .executeAsOneOrNull()
                ?.toRaqueta()
        }
    }
}
