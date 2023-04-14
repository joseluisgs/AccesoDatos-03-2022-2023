package services

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import database.ProductosQueries
import dev.joseluisgs.database.AppDatabase
import mu.KotlinLogging
import java.io.FileInputStream
import java.util.*

private val logger = KotlinLogging.logger {}

object SqlDeLightClient {
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

        productosQueries = JdbcSqliteDriver(connectionUrl).let { driver ->
            // Creamos la base de datos
            logger.debug { "SqlDeLightClient.init() - Create Schemas" }
            AppDatabase.Schema.create(driver)
            AppDatabase(driver)
        }.productosQueries

        // Limpiamos la base de datos
        if (initDatabase) {
            removeAllData()
        }

    }


    // limpiamos las tablas
    private fun removeAllData() {
        logger.debug { "SqlDeLightClient.removeAllData()" }
        productosQueries.transaction {
            logger.debug { "SqlDeLightClient.removeAllData() - Products " }
            productosQueries.removeAll()
        }
    }
}