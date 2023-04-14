import com.github.michaelbull.result.get
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import controllers.GuitarrasController
import locale.toLocalMoney
import models.Guitarra
import repositories.guitarras.GuitarrasRepositoryImpl
import services.guitarras.GuitarrasStorageImpl
import java.util.*

fun main(args: Array<String>) {
    println("Guitarras")

    // controlador con dependencias
    val guitarrrasController = GuitarrasController(
        guitarrasRepository = GuitarrasRepositoryImpl,
        guitarrasStorage = GuitarrasStorageImpl
    )

    // importar los datos
    guitarrrasController.importData(deleteAll = true)

    // Obtener todas las guitarras
    println()
    println("Obtener todas las guitarras")
    guitarrrasController.getAll().forEach { println(it.toStringLocale()) }

    // Obtener una guitarra por id
    println()
    println("Obtener una guitarra por id")
    guitarrrasController.getById(1)
        .onSuccess { println(it.toStringLocale()) }
        .onFailure { println("ERROR ${it.message}") }

    // Obtenemos una guitarra que no existe
    println()
    println("Obtener una guitarra que no existe")
    guitarrrasController.getById(-100)
        .onSuccess { println(it.toStringLocale()) }
        .onFailure { println("ERROR ${it.message}") }

    // Vamos a crear una guitarra
    println()
    println("Vamos a crear una guitarra")
    var newGuitar = Guitarra(
        uuid = UUID.randomUUID(),
        marca = "Mi guitarra New",
        modelo = "Mi modelo New",
        precio = 1000.0,
        stock = 10
    )

    guitarrrasController.save(newGuitar)
        .onSuccess { println(it.toStringLocale()) }
        .onFailure { println("ERROR ${it.message}") }

    println("Vamos a probar los errores de validacion")
    newGuitar = Guitarra(
        uuid = UUID.randomUUID(),
        marca = "Hola",
        modelo = "Mi modelo New",
        precio = 1000.0,
        stock = -10
    )

    guitarrrasController.save(newGuitar)
        .onSuccess { println(it.toStringLocale()) }
        .onFailure {
            println("ERROR ${it.message}")
        }

    // Vamos a actualizar una guitarra
    println()
    println("Vamos a actualizar una guitarra")
    val findGuitarra = guitarrrasController.getById(1).get()!!
    val updatedGuitarra = findGuitarra.copy(
        marca = "Mi guitarra Updated",
        modelo = "Mi modelo Updated",
        precio = 2000.0,
        stock = 20
    )

    guitarrrasController.save(updatedGuitarra)
        .onSuccess { println(it.toStringLocale()) }
        .onFailure { println("ERROR ${it.message}") }


    // valos a consultar la guitarra 1 otra vez
    println()
    println("Vamos a consultar la guitarra 1 otra vez")
    guitarrrasController.getById(1)
        .onSuccess { println(it.toStringLocale()) }
        .onFailure { println("ERROR ${it.message}") }

    // Vamos a eliminar una guitarra
    println()
    println("Vamos a eliminar una guitarra")
    guitarrrasController.delete(1L)
        .onSuccess { println("Guitarra eliminada") }
        .onFailure { println("ERROR ${it.message}") }

    val guitarras = guitarrrasController.getAll()

    // guitarra mas cara
    println()
    println("Guitarra mas cara")
    println(guitarras.maxBy { it.precio })

    // media de precio de guitarras
    println()
    println("Media de precio de guitarras")
    println(guitarras.map { it.precio }.average().toLocalMoney())

    // agrupar por marca
    println()
    println("Agrupar por marca")
    println(guitarras.groupBy { it.marca })

    // Cuantas guitarras hay por marca
    println()
    println("Cuantas guitarras hay por marca")
    println(guitarras.groupBy { it.marca }.mapValues { it.value.size })

    guitarrrasController.exportData()

}