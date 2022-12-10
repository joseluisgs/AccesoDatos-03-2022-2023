package db

import entities.Roles
import entities.Users
import io.r2dbc.spi.ConnectionFactories
import org.ufoss.kotysa.H2Tables
import org.ufoss.kotysa.r2dbc.sqlClient
import org.ufoss.kotysa.tables

object DataBaseManager {
    val client
        get() = ConnectionFactories
            .get("r2dbc:h2:mem:///testdb;DB_CLOSE_DELAY=-1")
            .sqlClient(getTables())

    // Obtenemos las tablas
    private fun getTables(): H2Tables {
        // Creamos un objeto H2Tables con las tablas de la base de datos
        // Entidades de la base de datos
        return tables()
            .h2(Roles, Users)
    }

}