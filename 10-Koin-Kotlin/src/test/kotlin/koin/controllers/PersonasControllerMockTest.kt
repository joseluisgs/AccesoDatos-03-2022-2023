package koin.controllers

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import koin.models.Persona
import koin.repositories.PersonasRepository
import koin.services.PersonasParser
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import java.util.*

/**
 * Creamos un mock de las depedencias para poder probar el controlador de nuestros casos qe tenemos.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockKExtension::class) // Necesario para poder usar los mocks
internal class PersonasControllerMockTest {
    // Definimos los mocks de las dependencias
    @MockK
    lateinit var repository: PersonasRepository

    @MockK
    lateinit var parser: PersonasParser

    // Inyectamos los mocks en el controlador
    @InjectMockKs
    lateinit var controller: PersonasController

    val persona = Persona(id = UUID.fromString("93a98d69-6da6-48a7-b34f-05b596ea839b"), nombre = "Pepe", edad = 23)
    val personaJson = """
        {
            "id": "93a98d69-6da6-48a7-b34f-05b596ea839b",
            "nombre": "Pepe",
            "edad": 23
        }
    """.trimIndent()

    init {
        MockKAnnotations.init(this)
    }

    @Test
    fun getPersonas() {
        every { repository.findAll() } returns listOf(persona)
        every { parser.encodeToString(listOf(persona)) } returns "[$personaJson]"

        val res = controller.getPersonas()
        assertAll(
            { assertTrue(res.contains(persona.nombre)) },
            { assertTrue(res.contains(persona.id.toString())) },
        )

        verify(exactly = 1) { repository.findAll() }
        verify { parser.encodeToString(listOf(persona)) }
    }

    @Test
    fun getPersonaById() {
        every { repository.findById(persona.id) } returns persona
        every { parser.encodeToString(persona) } returns personaJson

        val res = controller.getPersonaById(persona.id)
        assertAll(
            { assertTrue(res.contains(persona.nombre)) },
            { assertTrue(res.contains(persona.id.toString())) },
        )

        verify { repository.findById(persona.id) }
        verify { parser.encodeToString(persona) }
    }

    @Test
    fun getPersonaByNombre() {
        every { repository.findByNombre(persona.nombre) } returns listOf(persona)
        every { parser.encodeToString(listOf(persona)) } returns "[$personaJson]"

        val res = controller.getPersonaByNombre(persona.nombre)
        assertAll(
            { assertTrue(res.contains(persona.nombre)) },
            { assertTrue(res.contains(persona.id.toString())) },
        )
    }

    @Test
    fun createPersona() {
        every { repository.save(persona) } returns persona
        every { parser.encodeToString(persona) } returns personaJson

        val res = controller.createPersona(persona)

        assertAll(
            { assertTrue(res.contains(persona.nombre)) },
            { assertTrue(res.contains(persona.id.toString())) },
        )

        verify(exactly = 1) { repository.save(persona) }
        verify { parser.encodeToString(persona) }
    }

    @Test
    fun updatePersona() {
        every { repository.save(persona) } returns persona
        every { parser.encodeToString(persona) } returns personaJson

        val res = controller.createPersona(persona)

        assertAll(
            { assertTrue(res.contains(persona.nombre)) },
            { assertTrue(res.contains(persona.id.toString())) },
        )

        verify { repository.save(persona) }
        verify { parser.encodeToString(persona) }
    }

    @Test
    fun deletePersona() {
        every { repository.delete(persona) } returns persona
        every { parser.encodeToString(persona) } returns personaJson

        val res = controller.deletePersona(persona)

        assertAll(
            { assertTrue(res.contains(persona.nombre)) },
            { assertTrue(res.contains(persona.id.toString())) },
        )

        verify(exactly = 1) { repository.delete(persona) }
        verify { parser.encodeToString(persona) }
    }

    @Test
    fun deletePersonaById() {
        every { repository.deleteById(persona.id) } returns persona
        every { parser.encodeToString(persona) } returns personaJson

        val res = controller.deletePersonaById(persona.id)

        assertAll(
            { assertTrue(res.contains(persona.nombre)) },
            { assertTrue(res.contains(persona.id.toString())) },
        )

    }

    @Test
    fun getPersonaByIdException() {
        every { repository.findById(persona.id) } returns null

        // Con esto ya comprobamos que se lanza la excepción
        val res = assertThrows<Exception> { controller.getPersonaById(persona.id) }

        // Comprobamos que se llama al mensaje
        assert(res.message == "Persona  con id ${persona.id} no encontrada")

        verify { repository.findById(persona.id) }
    }

    @Test
    fun deletePersonaByIdException() {
        // Podemos meterle cualquier cosa!!! y no nos va a dar error
        every { repository.findById(any()) } returns null

        // Con esto ya comprobamos que se lanza la excepción
        val res = assertThrows<Exception> { controller.deletePersonaById(persona.id) }

        // Comprobamos que se llama al mensaje
        assert(res.message == "Persona  con id ${persona.id} no encontrada")

        verify { repository.findById(persona.id) }

    }
}

