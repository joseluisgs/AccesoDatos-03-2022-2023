package services.database

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import database.ProductosQueries
import dev.joseluisgs.database.AppDatabase
import mu.KotlinLogging
import org.koin.core.annotation.Singleton
import java.io.File
import java.io.FileInputStream
import java.nio.file.Files
import java.util.*

private val logger = KotlinLogging.logger {}

@Singleton
class SqlDeLightClient {
    private lateinit var connectionUrl: String

    // Podemos usar un driver de memoria o lo que queramos
    // En fichero
    // val driver: SqlDriver = JdbcSqliteDriver("jdbc:sqlite:./notes.db")

    lateinit var productosQueries: ProductosQueries
    private var initDatabase = true

    init {
        logger.debug { "Inicializando el gestor de Bases de Datos" }
        initConfig()
    }

    private fun initConfig() {
        // Leemos el fichero de configuración
        val propsFile = ClassLoader.getSystemResource("config.properties").file
        println(propsFile)
        propsFile?.let {
            logger.debug { "Cargando fichero de configuración de la Base de Datos: $propsFile" }
            val props = Properties()
            props.load(FileInputStream(propsFile))

            initDatabase = props.getProperty("database.initDatabase", "false").toBoolean()

            // Comentar o ajustar segun el tipo de base de datos y propiedades que se quieran usar
            connectionUrl =
                props.getProperty("database.connectionUrl", "jdbc:sqlite:Productos.db")

        }
        logger.debug { "Configuración de acceso a la Base de Datos cargada" }

        // Esto es una burrada, pero nos cargamos el fichero de la base de datos, esto lo hago para no meter todos
        // los datos como se puede ver en el método initDataExamples() ya que lo hago por el script
        Files.deleteIfExists(File(connectionUrl.removePrefix("jdbc:sqlite:")).toPath())


        productosQueries = JdbcSqliteDriver(connectionUrl).let { driver ->
            // Creamos la base de datos
            logger.debug { "SqlDeLightClient.init() - Create Schemas" }
            AppDatabase.Schema.create(driver)
            AppDatabase(driver)
        }.productosQueries

        // Limpiamos la base de datos
        /*if (initDatabase) {
            removeAllData()
            initDataExamples()
        }*/

    }

    private fun initDataExamples() {
        logger.debug { "SqlDeLightClient.initDataExamples()" }
        productosQueries.transaction {
            logger.debug { "SqlDeLightClient.initDataExamples() - Products " }
            productosQueries.insertProducto("Producto 1", 10, 10.0, "2023-04-04T17:40:36", "2023-04-04T17:40:36", 1)
            productosQueries.insertProducto("Producto 2", 20, 20.0, "2023-04-04T17:40:36", "2023-04-04T17:40:36", 0)
            productosQueries.insertProducto("Producto 3", 30, 30.0, "2023-04-04T17:40:36", "2023-04-04T17:40:36", 1)


        }
    }


    // limpiamos las tablas
    private fun removeAllData() {
        logger.debug { "SqlDeLightClient.removeAllData()" }
        /*productosQueries.transaction {
            productosQueries.removeAllLineasVentas()
            productosQueries.removeAllVentas()
            productosQueries.removeAllProductos()
        }*/
    }
}