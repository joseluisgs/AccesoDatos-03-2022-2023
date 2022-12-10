package db

import entities.RaquetasTable
import entities.TenistasTable
import io.r2dbc.spi.ConnectionFactories
import mu.KotlinLogging
import org.ufoss.kotysa.H2Tables
import org.ufoss.kotysa.r2dbc.sqlClient
import org.ufoss.kotysa.tables

private val logger = KotlinLogging.logger {}


object DataBaseManager {

    val client = ConnectionFactories
        .get("r2dbc:h2:mem:///tenistas;DB_CLOSE_DELAY=-1")
        .sqlClient(getTables())

    /*
        return ConnectionFactoryOptions.builder()
            .option(DRIVER, "h2")
            .option(PROTOCOL, "...") // file, mem
            .option(HOST, "…")
            .option(USER, "…")
            .option(PASSWORD, "…")
            .option(DATABASE, "…")
            .build()
            */


    // Obtenemos las tablas
    private fun getTables(): H2Tables {
        // Creamos un objeto H2Tables con las tablas de la base de datos
        // Entidades de la base de datos
        return tables()
            .h2(RaquetasTable, TenistasTable)
    }

    suspend fun createTables() {
        logger.debug { "Creando tablas" }
        client createTableIfNotExists RaquetasTable
        client createTableIfNotExists TenistasTable
    }

}