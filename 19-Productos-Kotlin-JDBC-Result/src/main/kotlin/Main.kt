import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import controllers.ProductosController
import factories.productoRandom
import models.Producto
import repositories.productos.ProductosRepositoryImpl
import services.DataBaseManager
import java.io.FileInputStream
import java.util.*

fun main(args: Array<String>) {
    println("Hola Bases de Datos!!!")

    initDataBase()

    //ejemploRepository()
    ejemploControlador()


}

fun ejemploControlador() {
    val productosController = ProductosController(ProductosRepositoryImpl())

    repeat(10) {
        productosController.save(productoRandom())
    }

    println("Todos los productos:")
    productosController.findAll().forEach { println(it) }

    println()
    println("Productos disponibles") // que solo son los disponibles
    productosController.findAllDisponible().forEach { println(it) }

    println()
    println("Productos NO disponibles") // que solo son los disponibles
    productosController.findAllDisponible(false).forEach { println(it) }

    println()
    println("Producto con id 1")
    productosController.findById(1).onSuccess { println(it.toLocalString()) }

    println()
    println("Producto con id 100")
    productosController.findById(100)
        .onSuccess { println(it.toLocalString()) }
        .onFailure { println(it.message) }

    println()
    println("Productos con nombre 'a'")
    productosController.findByNombre("a").forEach { println(it.toLocalString()) }

    println()
    println("Actualizar producto con id 1")
    productosController.update(
        Producto(
            id = 1,
            nombre = "Producto 1 Updated",
            precio = 100.0,
            cantidad = 10,
            disponible = false
        )
    ).onSuccess { println(it.toLocalString()) }

    println()
    println("Borrar producto con id 1")
    productosController.deleteById(1).onSuccess { println(it) }

    println("Todos los productos:")
    productosController.findAll().forEach { println(it) }


    // Probamos los errores
    println()
    println("Buscar producto con id -100")
    productosController.findById(-100).onFailure { println(it.message) }

    println()
    println("Borrar producto con id -100")
    productosController.deleteById(-100).onFailure { println(it.message) }

    println()
    println("Actualizar producto con id 100")
    productosController.update(
        Producto(
            id = 100,
            nombre = "Producto 100 Updated",
            precio = 100.0,
            cantidad = 10,
            disponible = false
        )
    ).onSuccess { println(it.toLocalString()) }
        .onFailure { println(it.message) }

    println()
    println("Insertar/actualizar producto con campos no validados")
    productosController.save(
        Producto(
            id = 100,
            nombre = "Producto 100 Updated",
            precio = 100.0,
            cantidad = -1,
            disponible = false
        )
    ).onSuccess { println(it.toLocalString()) }
        .onFailure { println(it.message) }


}

private fun ejemploRepository() {
    val productosRepository = ProductosRepositoryImpl()

    println("Todos los productos:")
    productosRepository.findAll().forEach { producto ->
        // println(producto.toLocalString())
        println(producto)
    }

    println()
    println("Productos con id 1:")
    val producto = productosRepository.findById(1)
    producto?.let {
        println(it)
    }

    println()
    println("Productos con uuid 5f5e58ec-e099-4ef6-ac49-ebde498c913e:")
    val producto2 = productosRepository.findByUuid("5f5e58ec-e099-4ef6-ac49-ebde498c913e")
    producto2?.let {
        println(it)
    }

    println()
    println("Productos con nombre 'Prod':")
    val productos2 = productosRepository.findByNombre("Prod")
    productos2.forEach { producto ->
        println(producto)
    }

    println()
    println("Insertando un producto:")
    val producto3 = productosRepository.save(Producto(nombre = "Producto 3 Insert", precio = 100.0, cantidad = 10))
    println(producto3)
    val producto9 = productosRepository.save(Producto(nombre = "Producto 9 Insert", precio = 100.0, cantidad = 10))
    println(producto9)

    println()
    println("Actualizando un producto:")
    val producto4 = productosRepository.update(
        producto3.copy(
            nombre = "Producto Updated",
            precio = 200.0,
            cantidad = 20,
            disponible = false
        )
    )
    println(producto4)

    println()
    println("Eliminando un producto:")
    val producto5 = productosRepository.deleteById(producto9.id)
    println(producto5)

    println("Todos los productos:")
    productosRepository.findAll().forEach { producto ->
        println(producto)
    }
}

fun initDataBase() {
    val propsFile = ClassLoader.getSystemResource("config.properties").file
    propsFile?.let {
        val props = Properties()
        props.load(FileInputStream(propsFile))
        val initDataBase = props.getProperty("database.initDatabase", "false").toBoolean()
        if (initDataBase) {
            println("Inicializando la base de datos")
            DataBaseManager.initData()
        }
    }
}
