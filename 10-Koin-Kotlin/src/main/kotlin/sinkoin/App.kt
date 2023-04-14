import kotlinx.serialization.json.Json
import nl.adaptivity.xmlutil.serialization.XML
import sinkoin.controllers.PersonasController
import sinkoin.models.Persona
import sinkoin.repositories.PersonasRepositoryImpl
import sinkoin.services.PersonasParserImpl

private val json = Json { prettyPrint = true }
private val xml = XML { indent = 4 }

fun main() {
    App.run()
}

private object App {
    fun run() {
        controllerJson()
        controllerXml()
    }

    private fun controllerJson() {
        println("Controller Json")

        val controller = PersonasController(
            PersonasRepositoryImpl(),
            PersonasParserImpl(Json { prettyPrint = true })
        )

        // Esta vez recibimos los datos en formato JSON
        val persona01 = Persona()
        val persona02 = Persona()

        // Creo dos personas
        println("Creo dos personas...")
        println(controller.createPersona(persona01))
        println(controller.createPersona(persona02))

        // buscar...
        println("Busco por id: ${persona01.id}")
        println(controller.getPersonaById(persona01.id))
        println("Buscar por nombre: ${persona01.nombre}")
        println(controller.getPersonaByNombre(persona01.nombre))

        // Listar todos
        println("Listar todos...")
        println(controller.getPersonas())

        // Actualizar
        println("Actualizar: ${persona01.id}")
        val persona03 = Persona(persona01.id, "Pepe", 30)
        println(controller.updatePersona(persona03))

        // Borrar
        println("Borrar: ${persona01.id}")
        println(controller.deletePersona(persona03))

        // Listar todos
        println("Listar todos...")
        println(controller.getPersonas())
    }

    private fun controllerXml() {
        println("Controller XML")

        val controller = PersonasController(
            PersonasRepositoryImpl(),
            PersonasParserImpl(XML { indent = 4 })
        )

        // Esta vez recibimos los datos en formato JSON
        val persona01 = Persona()
        val persona02 = Persona()

        // Creo dos personas
        println("Creo dos personas...")
        println(controller.createPersona(persona01))
        println(controller.createPersona(persona02))

        // buscar...
        println("Busco por id: ${persona01.id}")
        println(controller.getPersonaById(persona01.id))
        println("Buscar por nombre: ${persona01.nombre}")
        println(controller.getPersonaByNombre(persona01.nombre))

        // Listar todos
        println("Listar todos...")
        println(controller.getPersonas())

        // Actualizar
        println("Actualizar: ${persona01.id}")
        val persona03 = Persona(persona01.id, "Pepe", 30)
        println(controller.updatePersona(persona03))

        // Borrar
        println("Borrar: ${persona01.id}")
        println(controller.deletePersona(persona03))

        // Listar todos
        println("Listar todos...")
        println(controller.getPersonas())
    }
}