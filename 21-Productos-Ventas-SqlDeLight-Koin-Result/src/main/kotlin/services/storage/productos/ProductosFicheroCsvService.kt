package services.storage.productos

import config.AppConfig
import models.Producto
import mu.KotlinLogging
import org.koin.core.annotation.Named
import org.koin.core.annotation.Singleton
import java.io.File
import java.time.LocalDateTime

private val logger = KotlinLogging.logger {}

@Singleton
@Named("ProductosCsvService")
class ProductosFicheroCsvService(
    appConfig: AppConfig
) : ProductosStorageService {
    private val localFile = "${appConfig.APP_DATA}${File.separator}productos.csv"

    override fun saveAll(items: List<Producto>) {
        logger.debug { "Guardando productos en fichero csv" }
        val file = File(localFile)
        // Escribir el encabezado
        file.writeText("id,nombre,precio,cantidad,createdAt,updatedAt,disponible" + "\n")
        items.forEach {
            file.appendText("${it.id},${it.nombre},${it.precio},${it.cantidad},${it.createdAt},${it.updatedAt},${it.disponible}" + "\n")
        }
    }

    override fun loadAll(): List<Producto> {
        logger.debug { "Cargando productos desde fichero csv" }
        val file = File(localFile)
        // early return
        if (!file.exists()) return emptyList()
        // Leer el fichero
        return file.readLines()
            .drop(1)
            .map { linea -> linea.split(",") }
            .map { campos -> campos.map { it.trim() } } // trim() elimina espacios en blanco
            .map { campos ->
                Producto(
                    id = campos[0].toLong(),
                    nombre = campos[1],
                    precio = campos[2].toDouble(),
                    cantidad = campos[3].toInt(),
                    createdAt = LocalDateTime.parse(campos[4]),
                    updatedAt = LocalDateTime.parse(campos[5]),
                    disponible = campos[6].toBoolean()
                )

            }
    }
}