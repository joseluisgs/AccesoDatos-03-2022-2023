import java.io.FileInputStream
import java.util.*

object AppConflig {
    lateinit var databaseUrl: String
    lateinit var outputData: String

    init {
        // Leer el las propiedades
        println("Leyendo las propiedades")
        val fileProperties = ClassLoader.getSystemResource("config.properties").file
        val properties = Properties()
        properties.load(FileInputStream(fileProperties))
        databaseUrl = properties.getProperty("database.url", "jdbc:sqlite::memory:")
        outputData = properties.getProperty("output.data", "data")
    }
}