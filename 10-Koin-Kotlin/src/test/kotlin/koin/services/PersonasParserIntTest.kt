package koin.services

import koin.di.DiAnnotationModule
import koin.models.Persona
import org.junit.jupiter.api.*
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.qualifier.named
import org.koin.ksp.generated.module
import org.koin.test.KoinTest
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue


/**
 * Al ser un test de integración debo pasarle las dependencias que necesita el servicio
 */

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class PersonasParserIntTest : KoinTest { // sin koin test DiModuleTest() {

    // Si necesito inyectar algunos modulos porque no quiero mockearlos
    // Lo hago con el inject

    private val personasParser: PersonasParser by inject(qualifier = named("PersonasParserJson"))
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

    @Test
    fun encodeToString() {
        val res = personasParser.encodeToString(persona)
        assertAll(
            { assertTrue(res.contains(persona.nombre)) },
            { assertTrue(res.contains(persona.id.toString())) },
        )
    }

    @Test
    fun decodeFromString() {
        val p = personasParser.decodeFromString("[$personaJson]")[0]
        assertEquals(persona, p)
    }
}