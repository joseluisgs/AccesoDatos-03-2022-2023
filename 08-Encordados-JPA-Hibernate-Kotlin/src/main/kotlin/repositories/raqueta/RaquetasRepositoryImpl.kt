package repositories.raqueta

import db.HibernateManager
import db.HibernateManager.manager
import models.Raqueta
import mu.KotlinLogging
import java.util.*
import javax.persistence.TypedQuery


private val logger = KotlinLogging.logger {}

class RaquetasRepositoryImpl : RaquetasRepository {

    override fun findAll(): List<Raqueta> {
        logger.debug { "findAll()" }
        var raquetas = mutableListOf<Raqueta>()
        HibernateManager.query {
            val query: TypedQuery<Raqueta> = manager.createNamedQuery("Raqueta.findAll", Raqueta::class.java)
            raquetas = query.resultList
        }
        return raquetas
    }

    override fun findById(id: UUID): Raqueta? {
        logger.debug { "findById($id)" }
        var raqueta: Raqueta? = null
        HibernateManager.query {
            raqueta = manager.find(Raqueta::class.java, id)
        }
        return raqueta
    }

    fun findByMarca(marca: String): List<Raqueta> {
        logger.debug { "findByMarca($marca)" }
        var raquetas = mutableListOf<Raqueta>()
        HibernateManager.query {
            // Podemos definir la consulta sobre la marcha
            val query: TypedQuery<Raqueta> =
                manager.createQuery(
                    "SELECT r FROM Raqueta r WHERE r.marca = :marca",
                    Raqueta::class.java
                )
            query.setParameter("marca", marca)
            raquetas = query.resultList
        }
        return raquetas
    }

    override fun save(entity: Raqueta): Raqueta {
        logger.debug { "save($entity)" }
        HibernateManager.transaction {
            manager.merge(entity)
        }
        return entity
    }

    override fun delete(entity: Raqueta): Boolean {
        var result = false
        logger.debug { "delete($entity)" }
        HibernateManager.transaction {
            // Ojo que borrar implica que estemos en la misma sesión y nos puede dar problemas, por eso lo recuperamos otra vez
            val raqueta = manager.find(Raqueta::class.java, entity.uuid)
            raqueta?.let {
                manager.remove(it)
                result = true
            }
        }
        return result
    }
}

