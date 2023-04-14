package repositories.raqueta

import db.HibernateManager
import models.Raqueta
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

/**
 * Test de la clase RaquetasRepositoryImpl
 * Integramos con Hibernate, porque no se lo hemos pasado por parámetro como dependencia
 * Puedes intentarlo mockeando la clase HibernateManager con Mockito
 * Si lo consigues es mejor, porque así no dependemos de la base de datos
 *
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class RaquetasRepositoryImplTest {

    private val repository = RaquetasRepositoryImpl()

    @BeforeAll
    fun setUp() {
        // Borramos todas las raquetas
        HibernateManager.transaction {
            val query = HibernateManager.manager.createNativeQuery("DELETE FROM raquetas")
            query.executeUpdate()
        }

        // Creamos una raqueta de prueba
        HibernateManager.transaction {
            val raqueta = Raqueta(
                marca = "Wilson",
                precio = 199.95
            )
            HibernateManager.manager.merge(raqueta)
        }
        // El resto ya lo sabes... Es un CRUD
    }

    @Test
    fun findAll() {
    }

    @Test
    fun findById() {
    }

    @Test
    fun findByMarca() {
    }

    @Test
    fun save() {
    }

    @Test
    fun delete() {
    }
}