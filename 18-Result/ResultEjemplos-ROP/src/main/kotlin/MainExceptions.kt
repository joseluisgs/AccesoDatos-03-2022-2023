import controllers.PersonasExceptionController
import models.Persona
import respositories.PersonasRepository

fun main(args: Array<String>) {
    val controller = PersonasExceptionController(PersonasRepository)

    controller.getAll().forEach { println(it) }

    try {
        val persona = controller.getById(1)
        println(persona)
    } catch (e: Exception) {
        println(e.message)
    }

    try {
        val persona = controller.getById(-99)
        println(persona)
    } catch (e: Exception) {
        println(e.message)
    }

    try {
        val persona = controller.create(Persona(nombre = "Juan", edad = 20))
        println(persona)
    } catch (e: Exception) {
        println(e.message)
    }

    try {
        val persona = controller.create(Persona(nombre = "", edad = 20))
        println(persona)
    } catch (e: Exception) {
        println(e.message)
    }

    try {
        val persona = controller.create(Persona(nombre = "Juan", edad = -20))
        println(persona)
    } catch (e: Exception) {
        println(e.message)
    }

}