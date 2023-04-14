package utils

import mu.KotlinLogging
import java.io.IOException
import java.util.*

val logger = KotlinLogging.logger {}
class ApplicationProperties {
    private val properties: Properties = Properties()

    init {
        try {
            properties.load(javaClass.classLoader.getResourceAsStream("application.properties"))
        } catch (ex: IOException) {
            System.err.println("IOException Ocurrido al leer el fichero de propiedades: " + ex.message)
            logger.error { "IOException Ocurrido al leer el fichero de propiedades: " + ex.message }
        }
    }

    fun readProperty(keyName: String?): String {
        // Logger.getLogger(getClass().getName()).log(Level.INFO, "Leyendo propiedad " + keyName);
        return properties.getProperty(keyName, "No existe esa clave en el fichero de propiedades")
    }
}