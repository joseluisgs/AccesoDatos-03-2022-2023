package koin.repositories

import koin.models.Persona
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.util.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class PersonasRepositoryTest {

    private val personasRepository = PersonasRepositoryImpl()
    val persona = Persona(id = UUID.fromString("93a98d69-6da6-48a7-b34f-05b596ea839b"), nombre = "Pepe", edad = 23)

    @BeforeEach
    fun setUp() {
        personasRepository.save(persona)
    }

    @AfterEach
    fun tearDown() {
        personasRepository.deleteAll()
    }

    @Test
    fun findByNombre() {
        val p = personasRepository.findByNombre("Pepe")[0]
        assertEquals(persona, p)
    }

    @Test
    fun findAll() {
        val p = personasRepository.findAll()[0]
        assertEquals(persona, p)
    }

    @Test
    fun findById() {
        val p = personasRepository.findById(persona.id)
        assertEquals(persona, p)
    }

    @Test
    fun findByIdNoExiste() {
        val p = personasRepository.findById(UUID.fromString("93a98d6a-6da6-48a7-b34f-05b596ea839c"))
        assertNull(p)
    }

    @Test
    fun save() {
        val p = Persona()
        val res = personasRepository.save(p)
        assertEquals(res, p)
    }

    @Test
    fun delete() {
        personasRepository.delete(persona)
        val p = personasRepository.findById(persona.id)
        assertNull(p)
    }

    @Test
    fun deleteById() {
        personasRepository.deleteById(persona.id)
        val p = personasRepository.findById(persona.id)
        assertNull(p)
    }

    @Test
    fun deleteAll() {
        personasRepository.deleteAll()
        val p = personasRepository.findAll()
        assertTrue(p.isEmpty())
    }
}