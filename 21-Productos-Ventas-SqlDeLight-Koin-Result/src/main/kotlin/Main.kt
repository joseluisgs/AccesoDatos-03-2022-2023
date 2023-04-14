import com.github.michaelbull.result.get
import com.github.michaelbull.result.getOrThrow
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import config.AppConfig
import controllers.ProductosController
import controllers.VentasController
import factories.productoRandom
import models.User
import mu.KotlinLogging
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.GlobalContext.startKoin
import org.koin.ksp.generated.defaultModule
import org.koin.logger.slf4jLogger

private val logger = KotlinLogging.logger {}

fun main() {
    println("Hola Carro de Compra con Bases de Datos SqlDeLight + Koin")

    // Cargo el contexto de Koin de DI
    startKoin {
        slf4jLogger() // Logger de Koin para ver lo que hace opcional
        defaultModule() // Módulo por defecto de Koin con las dependencias
    }

    // Inicio la aplicación
    App().start()

}

class App : KoinComponent {
    fun start() {
        // Leemos la configuración de la aplicación
        val appConfig: AppConfig by inject()
        println("APP_NAME: ${appConfig.APP_NAME}")

        // usamos los controlaores ya con las dependencias
        val productosController: ProductosController by inject()
        val ventasController: VentasController by inject()


        // importamos los productos del CSV
        productosController.importData()

        // Operamos con los productos

        println("Todos los productos")
        productosController.findAll().forEach { println(it.toLocalString()) }

        println()
        println("Info Producto con ID 1")
        productosController.findById(1)
            .onSuccess { println(it.toLocalString()) }
            .onFailure { println(it.message) }


        println()
        println("Ponemos nuevo nombre no disponible el producto con ID 1")
        val producto = productosController.findById(1)
            .getOrThrow { RuntimeException("Esto es una excepción mía") } // Si no existe el producto, se lanza una excepción
        val updatedProducto = producto.copy(nombre = "Producto 1 Updated", disponible = false)
        productosController.update(updatedProducto)
            .onSuccess { println(it.toLocalString()) }
            .onFailure { println(it.message) }

        println()
        println("Insertamos un nuevo producto")
        val newProducto = productoRandom()
        productosController.save(newProducto)
            .onSuccess { println(it.toLocalString()) }
            .onFailure { println(it.message) }

        println()
        println("Borramos el producto con ID 1")
        productosController.deleteById(1)
            .onSuccess { println(it) }
            .onFailure { println(it.message) }

        println("Todos los productos")
        productosController.findAll().forEach { println(it.toLocalString()) }

        // Exportamos los productos a JSON
        println()
        println("Exportamos los productos a JSON")
        productosController.exportData()

        // caso incorrecto con un producto
        println()
        println("Insertamos un nuevo producto con un nombre no válido")
        val newProducto2 = productoRandom().copy(nombre = "")
        productosController.save(newProducto2)
            .onSuccess { println(it.toLocalString()) }
            .onFailure { println(it.message) }

        // caso incorrecto con un producto que no existe
        println()
        productosController.deleteById(-999)
            .onSuccess { println(it) }
            .onFailure { println(it.message) }


        // Vamos ahora con las ventas
        println()
        println("Venta con ID 1")
        val venta1 = ventasController.findById(1)
            .onSuccess { println(it.toLocalString()) }
            .onFailure { println(it.message) }

        // Sacamos una factura de la venta con ID 1
        println()
        println("Factura de la venta con ID 1")
        ventasController.exportInvoice(venta1.get()!!)

        println()
        println("Insertar nueva Venta")
        val user = User(1, "Juan", "juan@juan.com", "123456")
        val items = mapOf(2L to 2, 3L to 2) // Daría 60€
        val venta2 = ventasController.save(user.id, items)
            .onSuccess { println(it.toLocalString()) }
            .onFailure { println(it.message) }

        println()
        println("Factura de la venta con ID 2")
        ventasController.exportInvoice(venta2.get()!!)

        // Exportamos las ventas a JSON
        println()
        println("Exportamos los productos a JSON")
        ventasController.exportData()

        // Vamos a intentar hacer una venta vacía y con un usuario incorrecto
        println()
        println("Insertar nueva Venta vacía")
        ventasController.save(1L, mapOf())
            .onSuccess { println(it.toLocalString()) }
            .onFailure { println(it.message) }

        println()
        println("Insertar nueva Venta con un usuario que no es válido")
        ventasController.save(-1L, items)
            .onSuccess { println(it.toLocalString()) }
            .onFailure { println(it.message) }

        // una venta que no existe
        println()
        println("Venta con ID -999")
        ventasController.findById(-999)
            .onSuccess { println(it.toLocalString()) }
            .onFailure { println(it.message) }
    }
}

