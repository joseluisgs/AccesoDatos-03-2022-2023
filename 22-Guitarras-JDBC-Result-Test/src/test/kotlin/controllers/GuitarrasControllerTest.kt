package controllers

import com.github.michaelbull.result.get
import com.github.michaelbull.result.getError
import data.guitarras.getGuitarraInitData
import errors.GuitarraError
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import repositories.guitarras.GuitarrasRepository
import services.guitarras.GuitarrasStorage
import java.util.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockKExtension::class) // Vamos a usar Mockk
class GuitarrasControllerTest {

    @MockK
    private lateinit var repository: GuitarrasRepository

    @MockK
    private lateinit var storage: GuitarrasStorage

    @InjectMockKs
    private lateinit var controller: GuitarrasController

    val guitarras = getGuitarraInitData()

    // Es equivalente a esto
    // private var repo1 = mockk<Repository>()
    // private var controller1 = Controller(repo1)

    @BeforeEach
    fun setUp() {
    }

    @AfterEach
    fun tearDown() {
    }

    @Test
    fun getAll() {
        // Cuando se llame al método getAll del repositorio devolverá misMedicos
        every { repository.findAll() } returns guitarras

        val res = controller.getAll()

        assertAll(
            { assertNotNull(res) },
            { assertEquals(guitarras.size, res.size) },
        )

        // Verifico si realmente se ha llamado al método getAll del repositorio
        verify { repository.findAll() }
    }

    @Test
    fun getById() {
        every { repository.findById(any()) } returns guitarras[0]

        val res = controller.getById(1)

        assertAll(
            { assertNotNull(res) },
            { assertEquals(guitarras[0], res.get()) },
        )

        verify { repository.findById(any()) }
    }

    @Test
    fun getByIdNotFound() {
        every { repository.findById(any()) } returns null

        val res = controller.getById(1)

        assertAll(
            { assertNotNull(res) },
            { assertTrue(res.get() == null) },
            { assertTrue(res.getError() != null) },
            { assertTrue(res.getError() is GuitarraError.GuitarraNoEncontrada) },
            { assertEquals("No se ha encontrado la guitarra con id: 1", res.getError()?.message) },
        )

        verify { repository.findById(any()) }
    }


    @Test
    fun getByUuid() {
        every { repository.findByUuid(any()) } returns guitarras[0]

        val res = controller.getByUuid(UUID.randomUUID())

        assertAll(
            { assertNotNull(res) },
            { assertEquals(guitarras[0], res.get()) },
        )

        verify { repository.findByUuid(any()) }
    }

    @Test
    fun getByUuidNotFound() {
        every { repository.findByUuid(any()) } returns null

        val res = controller.getByUuid(UUID.randomUUID())

        assertAll(
            { assertNotNull(res) },
            { assertTrue(res.get() == null) },
            { assertTrue(res.getError() != null) },
            { assertTrue(res.getError() is GuitarraError.GuitarraNoEncontrada) },
            { assertTrue(res.getError()?.message!!.contains("No se ha encontrado la guitarra con uuid:")) },
        )

        verify { repository.findByUuid(any()) }
    }

    @Test
    fun getByMarca() {
        every { repository.findByMarca(any()) } returns guitarras

        val res = controller.getByMarca("Fender")

        assertAll(
            { assertNotNull(res) },
            { assertEquals(guitarras.size, res.size) },
        )

        verify { repository.findByMarca(any()) }
    }

    @Test
    fun save() {
        every { repository.save(any()) } returns guitarras[0]

        val res = controller.save(guitarras[0])

        assertAll(
            { assertNotNull(res) },
            { assertEquals(guitarras[0], res.get()) },
        )
    }

    @Test
    fun saveErrorMarca() {

        val guitarra = guitarras[0].copy(marca = "")

        val res = controller.save(guitarra)

        assertAll(
            { assertNotNull(res) },
            { assertTrue(res.get() == null) },
            { assertTrue(res.getError() != null) },
            { assertTrue(res.getError() is GuitarraError.MarcaNoValida) },
            { assertEquals("La marca no puede estar vacía", res.getError()?.message) },
        )

    }

    @Test
    fun saveErrorModelo() {

        val guitarra = guitarras[0].copy(modelo = "")

        val res = controller.save(guitarra)

        assertAll(
            { assertNotNull(res) },
            { assertTrue(res.get() == null) },
            { assertTrue(res.getError() != null) },
            { assertTrue(res.getError() is GuitarraError.ModeloNoValido) },
            { assertEquals("El modelo no puede estar vacío", res.getError()?.message) },
        )

    }

    @Test
    fun saveErrorPrecio() {


        val guitarra = guitarras[0].copy(precio = -1.0)

        val res = controller.save(guitarra)

        assertAll(
            { assertNotNull(res) },
            { assertTrue(res.get() == null) },
            { assertTrue(res.getError() != null) },
            { assertTrue(res.getError() is GuitarraError.PrecioNoValido) },
            { assertEquals("El precio no puede ser negativo", res.getError()?.message) },
        )

    }

    @Test
    fun saveErrorStock() {

        val guitarra = guitarras[0].copy(stock = -1)

        val res = controller.save(guitarra)

        assertAll(
            { assertNotNull(res) },
            { assertTrue(res.get() == null) },
            { assertTrue(res.getError() != null) },
            { assertTrue(res.getError() is GuitarraError.StockNoValido) },
            { assertEquals("El stock no puede ser negativo", res.getError()?.message) },
        )
        
    }

    @Test
    fun delete() {
        every { repository.deleteById(any()) } returns true

        val res = controller.delete(1)

        assertAll(
            { assertNotNull(res) },
            { assertTrue(res.get() == true) },
        )

        verify { repository.deleteById(any()) }
    }

    @Test
    fun deleteNotFound() {
        every { repository.deleteById(any()) } returns false

        val res = controller.delete(1)

        assertAll(
            { assertNotNull(res) },
            { assertTrue(res.get() == null) },
            { assertTrue(res.getError() != null) },
            { assertTrue(res.getError() is GuitarraError.GuitarraNoEncontrada) },
            { assertEquals("No se ha encontrado la guitarra con id: 1", res.getError()?.message) },
        )

        verify { repository.deleteById(any()) }
    }

}