package services.guitarras

import com.squareup.moshi.Moshi
import com.squareup.moshi.adapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import config.AppConfig
import dto.GuitarraDto
import exceptions.InputStorageException
import mappers.toGuitarraDto
import models.Guitarra
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

private val logger = mu.KotlinLogging.logger {}

object GuitarrasStorageImpl : GuitarrasStorage {
    override fun loadData(): List<Guitarra> {
        logger.debug { "Creando directorio ${AppConfig.dataInput}" }

        // Existe el fichero???
        val file = File(AppConfig.dataOutput + File.separator + "guitarras.csv")
        if (!file.exists() || !file.canRead()) {
            throw InputStorageException("El fichero no existe o no tiene permisos de lectura")
        }
        return file.readLines().drop(1)
            .map {
                it.split(",")
            }.map {
                it.map { item ->
                    item.trim()
                }
            }.map {
                Guitarra(
                    uuid = UUID.fromString(it[0]),
                    marca = it[1],
                    modelo = it[2],
                    precio = it[3].toDouble(),
                    stock = it[4].toInt(),
                )
            }
    }

    @OptIn(ExperimentalStdlibApi::class)
    override fun saveData(data: List<Guitarra>): Boolean {
        logger.debug { "save Data" }
        logger.debug { "Creando directorio ${AppConfig.dataOutput}" }
        Files.createDirectories(Paths.get(AppConfig.dataOutput))

        val file = File(AppConfig.dataOutput + File.separator + "guitarras.json")

        val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
        val jsonAdapter = moshi.adapter<List<GuitarraDto>>()

        file.writeText(
            jsonAdapter.indent("    ").toJson(data.map { it.toGuitarraDto() })
        )

        return true

    }

    override fun exportToCsv(data: List<Guitarra>) {
        // Creamos el directorio data si no existe

        logger.debug { "Creando directorio ${AppConfig.dataOutput}" }
        Files.createDirectories(Paths.get(AppConfig.dataOutput))

        val file = File(AppConfig.dataOutput + File.separator + "guitarras.csv")

        logger.debug { "Saving directorio ${file.name}" }
        file.writeText("uuid, marca, modelo, precio, stock \n")
        data.forEach {
            file.appendText(
                "${it.uuid}, ${it.marca}, ${it.modelo}, ${it.precio}, ${it.stock} \n"
            )
        }
    }
}