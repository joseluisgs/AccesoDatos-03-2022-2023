package koin.services

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import koin.models.Persona
import kotlinx.serialization.StringFormat
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import java.util.*
import kotlin.test.assertEquals


/**
 * Creamos un mock de las depedencias para poder probar el controlador de nuestros casos qe tenemos.
 */

@ExtendWith(MockKExtension::class) // Necesario para poder usar los mocks
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class PersonasParserMockTest {

    @MockK
    lateinit var format: StringFormat

    @InjectMockKs
    lateinit var parser: PersonasParserImpl

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
    fun encodeToString() {
        every { format.encodeToString(persona) } returns personaJson

        val res = parser.encodeToString(persona)
        assertEquals(personaJson, res)

        verify { format.encodeToString(persona) }
    }

    @Test
    fun decodeFromString() {
        every { format.decodeFromString<List<Persona>>(any()) } returns listOf(persona)

        val res = parser.decodeFromString("[$personaJson]")[0]

        assertAll(
            { assertEquals(persona.id, res.id) },
            { assertEquals(persona.nombre, res.nombre) },
            { assertEquals(persona.edad, res.edad) },
        )

        verify { format.decodeFromString<List<Persona>>(any()) }


    }

}