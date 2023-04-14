package config

import java.io.FileInputStream
import java.util.*


private val logger = mu.KotlinLogging.logger {}

object AppConfig {
    private var _dataInput: String = "data"
    val dataInput get() = _dataInput

    private var _dataOutput: String = "data"
    val dataOutput get() = _dataOutput

    private var _databaseUrl: String = "jdbc:sqlite:database.db"
    val databaseUrl get() = _databaseUrl

    private var _databaseDropTable: Boolean = false
    val databaseDropTable get() = _databaseDropTable


    init {
        loadProperties()
    }

    // lodad properties
    private fun loadProperties() {
        logger.debug { "Loading properties" }
        // Accedemos al fichero de propiedades
        val propertiesFile = ClassLoader.getSystemResource("application.properties").file
        val properties = Properties()
        // Las cargamos al fichero de de propiedades
        properties.load(FileInputStream(propertiesFile))
        _dataInput = properties.getProperty("data.input", "data")
        _dataOutput = properties.getProperty("data.output", "data")
        _databaseUrl = properties.getProperty("database.url", "jdbc:sqlite:database.db")
        _databaseDropTable = properties.getProperty("database.droptable", "false").toBoolean()
    }
}