package koin

import koin.controllers.PersonasController
import koin.di.DiAnnotationModule
import koin.models.Persona
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.GlobalContext.startKoin
import org.koin.ksp.generated.module

// Anotaciones, para cosas simples o por defecto
// https://insert-koin.io/docs/quickstart/kotlin

// Manual con DSL, para cosas complejas
// https://insert-koin.io/docs/quickstart/kotlin

// Main con Koin
fun main() {
    startKoin {
        printLogger()
        // Puedo meter todos los módulos que quiera y en cada uno configurarlo como guste
        modules(
            DiAnnotationModule().module
        )
    }
    KoinApp().run()
}

class KoinApp : KoinComponent {
    // No es necesario llegar a tal extremo!!
    //private val controller: PersonasController by inject(qualifier = named("PersonasControllerJson"))
    // Por defecto con anotaciones
    private val controller: PersonasController by inject()

    // Creado manualmente para Json
    // private val controller: PersonasController by inject(qualifier = named("PersonasControllerJson"))
    // Creado manualmente para XML
    //private val controller: PersonasController by inject(qualifier = named("PersonasControllerXml"))

    // Creado manualmente para Json con DSL Constructor
    // private val controller: PersonasController by inject(qualifier = named("PersonasControllerJsonLambda"))
    // Creado manualmente para XML con DSL Constructor
    //private val controller: PersonasController by inject(qualifier = named("PersonasControllerXmlLambda"))

    fun run() {
        controller()
    }

    private fun controller() {
        println("Controller")

        /*val controller = PersonasController(
            PersonasRepositoryImpl(),
            PersonasParserImpl(Json { prettyPrint = true })
        )*/

        // Esta vez recibimos los datos en formato JSON
        val persona01 = Persona()
        val persona02 = Persona()

        // Creo dos personas
        println("Creo dos personas...")
        println(this.controller.createPersona(persona01))
        println(this.controller.createPersona(persona02))

        // buscar...
        println("Busco por id: ${persona01.id}")
        println(this.controller.getPersonaById(persona01.id))
        println("Buscar por nombre: ${persona01.nombre}")
        println(this.controller.getPersonaByNombre(persona01.nombre))

        // Listar todos
        println("Listar todos...")
        println(this.controller.getPersonas())

        // Actualizar
        println("Actualizar: ${persona01.id}")
        val persona03 = Persona(persona01.id, "Pepe", 30)
        println(this.controller.updatePersona(persona03))

        // Borrar
        println("Borrar: ${persona01.id}")
        println(this.controller.deletePersona(persona03))

        // Listar todos
        println("Listar todos...")
        println(this.controller.getPersonas())
    }
}