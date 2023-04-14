package config

import mu.KotlinLogging
import org.koin.core.annotation.Singleton
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*


private val logger = KotlinLogging.logger {}

private val LOCAL_PATH = "${System.getProperty("user.dir")}${File.separator}"

@Singleton
class AppConfig {
    val APP_NAME = "Kotlin Productos Ventas"
    val APP_VERSION = "1.0.0"
    lateinit var APP_AUTHOR: String
    lateinit var APP_DATA: String


    init {
        loadProperties()
        initStorage()
    }

    private fun initStorage() {
        // Crear el directorio data si no existe
        logger.debug { "Creando directorio data si no existe" }
        Files.createDirectories(Paths.get(APP_DATA))
    }

    private fun loadProperties() {
        logger.debug { "Cargando configuración de la aplicación" }
        val properties = Properties()
        properties.load(AppConfig::class.java.getResourceAsStream("/config.properties"))
        // properties.load(ClassLoader.getSystemResourceAsStream("/config.properties")) --> Aquí falla!!
        /*properties.forEach { (key, value) ->
            logger.debug { "Configuración: $key = $value" }
        }*/
        APP_AUTHOR = properties.getProperty("app.author") ?: "1DAM"
        APP_DATA = properties.getProperty("app.storage.dir") ?: "data"
        APP_DATA = "$LOCAL_PATH$APP_DATA"

        logger.debug { "Configuración: app.author = $APP_AUTHOR" }
        logger.debug { "Configuración: app.storage.dir = $APP_DATA" }
    }

}