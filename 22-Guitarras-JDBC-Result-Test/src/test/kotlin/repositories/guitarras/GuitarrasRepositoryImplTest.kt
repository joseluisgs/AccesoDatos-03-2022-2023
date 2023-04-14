package repositories.guitarras

import data.guitarras.getGuitarraInitData
import models.Guitarra
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import java.nio.file.Files
import java.util.*
import kotlin.io.path.Path

/**
 * NOTA!!!
 * Esta cogiendo el application.properties de la carpeta resources que está en TEST!!!
 * Esta es la gracia de tener los ficheros de configuración en la carpeta resources
 * y así adaptarlos
 */

@TestInstance(TestInstance.Lifecycle.PER_CLASS) // Para que los métodos de prueba sean estáticos
class GuitarrasRepositoryImplTest {

    var repository = GuitarrasRepositoryImpl

    var testId = 0L

    @BeforeAll
    fun setUpAll() {
        // Borramos la base de datos
        Files.deleteIfExists(Path("test.db"))
        repository.saveAll(getGuitarraInitData())
    }

    @AfterAll
    fun tearDownAll() {
        // Borramos la base de datos
        Files.deleteIfExists(Path("test.db"))
    }

    @Test
    fun findAll() {
        val guitarras = repository.findAll()
        assertEquals(4, guitarras.count())
    }

    @Test
    fun findById() {
        val guitarra = repository.findById(1)
        assertAll(
            { assertNotNull(guitarra) },
            { assertEquals("Gibson", guitarra?.marca) },
            { assertEquals("Les Paul Standard", guitarra?.modelo) },
            { assertEquals(2333.0, guitarra?.precio) },
            { assertEquals(10, guitarra?.stock) }
        )
    }

    @Test
    fun existsById() {
        // insertamos una guitarra de prueba
        val newGuitar = repository.save(
            Guitarra(
                uuid = UUID.randomUUID(),
                marca = "Test Marca Exists",
                modelo = "Test Modelo Exists",
                precio = 1000.0,
                stock = 1
            )
        )
        // Comprobamos que existe
        assertTrue(repository.existsById(newGuitar.id))
    }

    @Test
    fun saveNew() {
        val newGuitar = repository.save(
            Guitarra(
                uuid = UUID.randomUUID(),
                marca = "Test Marca New",
                modelo = "Test Modelo New",
                precio = 1000.0,
                stock = 1
            )
        )
        assertAll(
            { assertNotNull(newGuitar) },
            { assertEquals("Test Marca New", newGuitar.marca) },
            { assertEquals("Test Modelo New", newGuitar.modelo) },
            { assertEquals(1000.0, newGuitar.precio) },
            { assertEquals(1, newGuitar.stock) }
        )
        // Cogemos el id para usarlo en el test de update
        testId = newGuitar.id
    }

    @Test
    fun saveUpdate() {
        val updateGuitar = repository.save(
            Guitarra(
                id = testId,
                uuid = UUID.randomUUID(),
                marca = "Test Marca Update",
                modelo = "Test Modelo Update",
                precio = 2000.0,
                stock = 2
            )
        )
        assertAll(
            { assertNotNull(updateGuitar) },
            { assertEquals("Test Marca Update", updateGuitar.marca) },
            { assertEquals("Test Modelo Update", updateGuitar.modelo) },
            { assertEquals(2000.0, updateGuitar.precio) },
            { assertEquals(2, updateGuitar.stock) }
        )
    }


    @Test
    fun deleteById() {
        // Insertamos una guitarra de prueba
        val newGuitar = repository.save(
            Guitarra(
                uuid = UUID.randomUUID(),
                marca = "Test Marca Delete",
                modelo = "Test Modelo Delete",
                precio = 1000.0,
                stock = 1
            )
        )
        // Comprobamos que existe
        assertTrue(repository.existsById(newGuitar.id))
        // Borramos la guitarra
        repository.deleteById(newGuitar.id)
        // Comprobamos que ya no existe
        assertFalse(repository.existsById(newGuitar.id))
    }

    @Test
    fun deleteByIdNotFound() {
        // Borramos una guitarra que no existe
        assertFalse(repository.deleteById(-5))
    }

    @Test
    fun delete() {
        // insertamos una guitarra de prueba
        val newGuitar = repository.save(
            Guitarra(
                uuid = UUID.randomUUID(),
                marca = "Test Marca Delete",
                modelo = "Test Modelo Delete",
                precio = 1000.0,
                stock = 1
            )
        )
        // Comprobamos que existe
        assertTrue(repository.existsById(newGuitar.id))
        // Borramos la guitarra
        repository.delete(newGuitar)
        // Comprobamos que ya no existe
        assertFalse(repository.existsById(newGuitar.id))
    }

    @Test
    fun deleteAll() {
        // borramos todo
        repository.deleteAll()
        // comprobamos que no hay nada
        assertEquals(0, repository.count())
    }

    @Test
    fun saveAll() {
        // borramos todo
        repository.deleteAll()
        // insertamos 4 guitarras
        repository.saveAll(getGuitarraInitData())
        // comprobamos que hay 4
        assertEquals(4, repository.count())
    }

    @Test
    fun count() {
        // borramos todo
        repository.deleteAll()
        // insertamos 4 guitarras
        repository.saveAll(getGuitarraInitData())
        // comprobamos que hay 4
        assertEquals(4, repository.count())
    }

    @Test
    fun findByUuid() {
        // insertamos una guitarra de prueba
        val newGuitar = repository.save(
            Guitarra(
                uuid = UUID.randomUUID(),
                marca = "Test Marca Uuid",
                modelo = "Test Modelo Uuid",
                precio = 1000.0,
                stock = 1
            )
        )
        // Comprobamos que existe
        assertNotNull(repository.findByUuid(newGuitar.uuid))
    }

    @Test
    fun findByMarca() {
        // insertamos una guitarra de prueba
        val newGuitar = repository.save(
            Guitarra(
                uuid = UUID.randomUUID(),
                marca = "Test Marca Marca",
                modelo = "Test Modelo Marca",
                precio = 1000.0,
                stock = 1
            )
        )
        // Comprobamos que existe
        assertNotNull(repository.findByMarca(newGuitar.marca))
    }
}