import controllers.PersonasResultSimpleController
import models.Persona
import respositories.PersonasRepository

fun main(args: Array<String>) {
    val controller = PersonasResultSimpleController(PersonasRepository)

    controller.getAll().forEach { println(it) }

    var personaResult = controller.getById(1)
    personaResult.onSuccess { println(it) }
    personaResult.onError { println(it.message) }

    personaResult = controller.getById(-99)
    personaResult.onSuccess { println(it) }
    personaResult.onError { println(it.message) }


    personaResult = controller.create(Persona(nombre = "Juan", edad = 20))
    personaResult.onSuccess { println(it) }
    personaResult.onError { println(it.message) }

    personaResult = controller.create(Persona(nombre = "", edad = 20))
    personaResult.onSuccess { println(it) }
    personaResult.onError { println(it.message) }

    personaResult = controller.create(Persona(nombre = "Juan", edad = -20))
    personaResult.onSuccess { println(it) }
    personaResult.onError { println(it.message) }

}