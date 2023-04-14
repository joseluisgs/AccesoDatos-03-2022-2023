import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import controllers.PersonasResultErrorController
import errors.PersonasError
import models.Persona
import respositories.PersonasRepository

fun main(args: Array<String>) {
    val controller = PersonasResultErrorController(PersonasRepository)

    controller.getAll().forEach { println(it) }

    var personaResult = controller.getById(1)
    personaResult.onSuccess { println(it) }
    personaResult.onFailure { println(it.mensage) }

    personaResult = controller.getById(-99)
    personaResult.onSuccess { println(it) }
    personaResult.onFailure { println(it.mensage) }

    when (personaResult) {
        is Ok -> println(personaResult.value)
        is Err -> {
            when (personaResult.error) {
                is PersonasError.PersonaNoEncontradaError -> println(personaResult.error.mensage)
                is PersonasError.PersonaDatosNoValidosError -> println(personaResult.error.mensage) // Aquí no tiene sentido!!
            }
        }
    }


    personaResult = controller.create(Persona(nombre = "Juan", edad = 20))
    personaResult.onSuccess { println(it) }
    personaResult.onFailure { println(it.mensage) }

    personaResult = controller.create(Persona(nombre = "", edad = 20))
    personaResult.onSuccess { println(it) }
    personaResult.onFailure { println(it.mensage) }

    personaResult = controller.create(Persona(nombre = "Juan", edad = -20))
    personaResult.onSuccess { println(it) }
    personaResult.onFailure { println(it.mensage) }

    // De esta manera puedo manejar los errores de forma más clara y sacar provecho de la jerarquía de errores
    // para mensajes de error o ventanas!!!
    when (personaResult) {
        is Ok -> println(personaResult.value)
        is Err -> {
            when (personaResult.error) {
                is PersonasError.PersonaNoEncontradaError -> println(personaResult.error.mensage)
                is PersonasError.PersonaDatosNoValidosError -> println(personaResult.error.mensage)
            }
        }
    }

}