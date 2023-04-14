package koin.controllers

import koin.di.DiAnnotationModule
import koin.models.Persona
import koin.repositories.PersonasRepository
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Assertions.assertAll
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.qualifier.named
import org.koin.ksp.generated.module
import org.koin.test.KoinTest
import org.koin.test.inject
import java.util.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
/**
 * Al ser un test de integración debo pasarle las dependencias que necesita el servicio
 */
internal class PersonasControllerIntTest : KoinTest { // sin koin test DiModuleTest() {

    private val controller: PersonasController by inject()

    val persona = Persona(id = UUID.fromString("93a98d69-6da6-48a7-b34f-05b596ea839b"), nombre = "Pepe", edad = 23)
    val personaJson = """
        {
            "id": "93a98d69-6da6-48a7-b34f-05b596ea839b",
            "nombre": "Pepe",
            "edad": 23
        }
    """.trimIndent()

    @BeforeAll
    fun setUpDiModule() {
        startKoin {
            // Indicamos los módulos que queremos usar
            modules(DiAnnotationModule().module)
        }
    }

    @AfterAll
    fun tearDownDiModule() {
        stopKoin()
    }

    @BeforeEach
    fun setUp() {
        val repository: PersonasRepository by inject(qualifier = named("PersonasRepository"))
        repository.save(persona)
    }

    @AfterEach
    fun tearDown() {
        val repository: PersonasRepository by inject(qualifier = named("PersonasRepository"))
        repository.deleteAll()
    }

    @Test
    fun getPersonas() {
        val res = controller.getPersonas()
        assertAll(
            { assertTrue(res.contains(persona.nombre)) },
            { assertTrue(res.contains(persona.id.toString())) },
        )
    }

    @Test
    fun getPersonaById() {
        val res = controller.getPersonaById(persona.id)
        assertAll(
            { assertTrue(res.contains(persona.nombre)) },
            { assertTrue(res.contains(persona.id.toString())) },
        )
    }

    @Test
    fun getPersonaByNombre() {
        val res = controller.getPersonaByNombre(persona.nombre)
        assertAll(
            { assertTrue(res.contains(persona.nombre)) },
            { assertTrue(res.contains(persona.id.toString())) },
        )
    }

    @Test
    fun createPersona() {
        val res = controller.createPersona(persona)
        assertAll(
            { assertTrue(res.contains(persona.nombre)) },
            { assertTrue(res.contains(persona.id.toString())) },
        )
    }

    @Test
    fun updatePersona() {
        val res = controller.updatePersona(persona)
        assertAll(
            { assertTrue(res.contains(persona.nombre)) },
            { assertTrue(res.contains(persona.id.toString())) },
        )
    }

    @Test
    fun deletePersona() {
        val res = controller.deletePersona(persona)
        assertAll(
            { assertTrue(res.contains(persona.nombre)) },
            { assertTrue(res.contains(persona.id.toString())) },
        )
    }

    @Test
    fun deletePersonaById() {
        val res = controller.deletePersonaById(persona.id)
        assertAll(
            { assertTrue(res.contains(persona.nombre)) },
            { assertTrue(res.contains(persona.id.toString())) },
        )
    }

    @Test
    fun getPersonaByIdException() {
        val randomID = UUID.randomUUID()
        val res = assertThrows<Exception> { controller.getPersonaById(randomID) }

        // Comprobamos que se llama al mensaje
        assert(res.message == "Persona  con id $randomID no encontrada")
    }

}

