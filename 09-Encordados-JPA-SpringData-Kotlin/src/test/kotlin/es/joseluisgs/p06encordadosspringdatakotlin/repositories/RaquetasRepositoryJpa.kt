package es.joseluisgs.p06encordadosspringdatakotlin.repositories

import es.joseluisgs.p06encordadosspringdatakotlin.models.Raqueta
import es.joseluisgs.p06encordadosspringdatakotlin.repositories.raqueta.RaquetasRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache
import org.springframework.boot.test.autoconfigure.filter.TypeExcludeFilters
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTypeExcludeFilter
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.transaction.annotation.Transactional

/**
 * CUIDADO QUE esta ejecutando el CommandLineRunner por lo que debes tener cuidado con los datos que inserta
 * O que ya existan los datos que inserta
 * De esta manera podemos hacer test de los repositorios
 * moqueando el hibernate y el JPA integrado de SpringBoot
 */

// @DataJpaTest // -- Si esta no te va usa estas, que es la equivalencia
// Desde Aqui
@SpringBootTest
@Transactional
@TypeExcludeFilters(value = [DataJpaTypeExcludeFilter::class])
@AutoConfigureCache
@AutoConfigureDataJpa
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@AutoConfigureTestEntityManager
@ImportAutoConfiguration
// Hast aqui
// @TestInstance(TestInstance.Lifecycle.PER_CLASS) // Si hay BeforeAll y AfterAll en Kotlin
class RaquetasRepositoryTestJpa
@Autowired constructor(
    private val entityManager: TestEntityManager,
    private val raquetasRepository: RaquetasRepository
) {
    private val raquetaTest = Raqueta(marca = "Test", precio = 100.0)

    // Para borrar los datos de la tabla del CommandLineRunner
    // No es necesario porque mi EntitManager es de Test no afecta a lo que hay!!!
    /*@BeforeEach
    fun deleteAll() {
        for (element in raquetasRepository.findAll()) {
            entityManager.remove(element)
        }
    }*/

    @Test
    fun findByIdTest() {
        /**
         * Creamos un objeto raqueta y lo mockeamos en la BBDD
         */
        val res = entityManager.merge(raquetaTest)
        entityManager.flush()

        val sal = raquetasRepository.findByIdOrNull(res.uuid)
        assertEquals(sal, res)
    }

    @Test
    fun findAllTest() {
        /**
         * Creamos un objeto raqueta y lo mockeamos en la BBDD
         */
        val res = entityManager.merge(raquetaTest)
        entityManager.flush()

        val sal = raquetasRepository.findAll()
        println(sal)
        assertTrue(sal.contains(res))
    }

    @Test
    fun saveTest() {
        /**
         * Creamos un objeto raqueta y lo mockeamos en la BBDD
         */
        val res = entityManager.merge(raquetaTest)
        entityManager.flush()

        val sal = raquetasRepository.save(res)
        assertEquals(sal, res)
    }

    @Test
    fun deleteTest() {
        /**
         * Creamos un objeto raqueta y lo mockeamos en la BBDD
         *
         */
        val res = entityManager.merge(raquetaTest)
        entityManager.flush()

        raquetasRepository.delete(res)
        val sal = raquetasRepository.findByIdOrNull(res.uuid)
        assertNull(sal)
    }


}