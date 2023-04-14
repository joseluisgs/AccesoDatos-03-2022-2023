package repositories.tenista

import db.HibernateManager
import db.HibernateManager.manager
import models.Tenista
import mu.KotlinLogging
import java.util.*
import javax.persistence.TypedQuery


private val logger = KotlinLogging.logger {}

class TenistasRepositoryImpl : TenistasRepository {
    override fun findAll(): List<Tenista> {
        logger.debug { "findAll()" }
        var tenistas = mutableListOf<Tenista>()
        HibernateManager.query {
            val query: TypedQuery<Tenista> = manager.createNamedQuery("Tenista.findAll", Tenista::class.java)
            tenistas = query.resultList
        }
        return tenistas
    }

    override fun findById(id: UUID): Tenista? {
        logger.debug { "findById($id)" }
        var tenista: Tenista? = null
        HibernateManager.query {
            tenista = manager.find(Tenista::class.java, id)
        }
        return tenista
    }

    override fun save(entity: Tenista): Tenista {
        logger.debug { "save($entity)" }
        HibernateManager.transaction {
            manager.merge(entity)
        }
        return entity
    }

    override fun delete(entity: Tenista): Boolean {
        var result = false
        logger.debug { "delete($entity)" }
        HibernateManager.transaction {
            // Ojo que borrar implica que estemos en la misma sesión y nos puede dar problemas, por eso lo recuperamos otra vez
            val tenista = manager.find(Tenista::class.java, entity.uuid)
            tenista?.let {
                manager.remove(it)
                result = true
            }
            // manager.remove(entity)
        }
        return result
    }
}

