package services.database

import config.AppConfig
import mu.KotlinLogging
import java.sql.DriverManager

private val logger = KotlinLogging.logger { }

object DataBaseService {
    val db
        get() = DriverManager.getConnection(AppConfig.databaseUrl)


    init {
        logger.debug { "Init DataBaseService" }
        if (AppConfig.databaseDropTable) {
            dropTables()
        }
        createTables()
    }

    private fun dropTables() {
        val sql = "DROP TABLE IF EXISTS guitarras"
        logger.debug { "Drop Tables" }
        db.use {
            it.createStatement().use { stm ->
                stm.executeUpdate(sql)
            }
        }
    }

    private fun createTables() {
        val sql = """
            CREATE TABLE IF NOT EXISTS guitarras(
                id         INTEGER PRIMARY KEY AUTOINCREMENT,
                uuid       TEXT    NOT NULL,
                marca      TEXT    NOT NULL,
                modelo     TEXT    NOT NULL,
                precio     REAL    NOT NULL,
                stock      INTEGER NOT NULL,
                created_at TEXT    NOT NULL,
                updated_at TEXT    NOT NULL,
                deleted    INTEGER NOT NULL
            )
        """.trimIndent()
        logger.debug { "Create Tables" }
        db.use {
            it.createStatement().use { stm ->
                stm.executeUpdate(sql)
            }
        }

    }
}