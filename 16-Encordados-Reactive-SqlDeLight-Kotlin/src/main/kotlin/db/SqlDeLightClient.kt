package db

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import database.AppDatabase
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

object SqlDeLightClient {
    // Podemos usar un driver de memoria o lo que queramos
    private val driver: SqlDriver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)

    init {
        // Creamos la base de datos
        logger.debug { "SqlDeLightClient.init() - Create Schemas" }
        AppDatabase.Schema.create(driver)
    }

    val queries = AppDatabase(driver).appDatabaseQueries

    fun removeAllData() {
        logger.debug { "SqlDeLightClient.removeAllData()" }
        queries.transaction {
            logger.debug { "SqlDeLightClient.removeAllData() - Raquetas " }
            queries.removeAllRaquetas()
            logger.debug { "SqlDeLightClient.removeAllData() - Tenistas " }
            queries.removeAllTenistas()
        }
    }

}