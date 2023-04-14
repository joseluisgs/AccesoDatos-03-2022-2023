package repositories.guitarras

import models.Guitarra
import mu.KotlinLogging
import services.database.DataBaseService
import java.sql.Statement
import java.time.LocalDateTime
import java.util.*

private val logger = KotlinLogging.logger { }

object GuitarrasRepositoryImpl : GuitarrasRepository {
    override fun findAll(): Iterable<Guitarra> {
        logger.debug { "findAll" }

        val items = mutableListOf<Guitarra>()

        val sql = "SELECT * FROM guitarras"
        DataBaseService.db.use {
            it.prepareStatement(sql).use { stm ->
                val rs = stm.executeQuery()
                while (rs.next()) {
                    items.add(
                        Guitarra(
                            id = rs.getLong("id"),
                            uuid = UUID.fromString(rs.getString("uuid")),
                            marca = rs.getString("marca"),
                            modelo = rs.getString("modelo"),
                            precio = rs.getDouble("precio"),
                            stock = rs.getInt("stock"),
                            createdAt = LocalDateTime.parse(rs.getString("created_at")),
                            updatedAt = LocalDateTime.parse(rs.getString("updated_at")),
                            deleted = rs.getBoolean("deleted")
                        )
                    )
                }
            }
        }

        return items
    }

    override fun findById(id: Long): Guitarra? {
        logger.debug { "findById" }

        var item: Guitarra? = null

        val sql = "SELECT * FROM guitarras WHERE id = ?"
        DataBaseService.db.use {
            it.prepareStatement(sql).use { stm ->
                // Le paso los parametros
                stm.setLong(1, id)

                val rs = stm.executeQuery()

                if (rs.next()) {
                    item = Guitarra(
                        id = rs.getLong("id"),
                        uuid = UUID.fromString(rs.getString("uuid")),
                        marca = rs.getString("marca"),
                        modelo = rs.getString("modelo"),
                        precio = rs.getDouble("precio"),
                        stock = rs.getInt("stock"),
                        createdAt = LocalDateTime.parse(rs.getString("created_at")),
                        updatedAt = LocalDateTime.parse(rs.getString("updated_at")),
                        deleted = rs.getBoolean("deleted")
                    )
                }
            }
        }
        return item
    }

    override fun existsById(id: Long): Boolean {
        logger.debug { "existsById" }

        return findById(id) != null
    }

    override fun save(entity: Guitarra): Guitarra {
        logger.debug { "save" }

        // Preguntar por la clave = 0
        return if (existsById(entity.id)) {
            update(entity)
        } else {
            create(entity)
        }
    }

    private fun create(entity: Guitarra): Guitarra {
        logger.debug { "create" }

        val createdTime = LocalDateTime.now()
        var myId: Long = 0

        val sql = """
            INSERT INTO guitarras VALUES (null, ?,?,?,?,?,?,?,?)
        """.trimIndent()
        DataBaseService.db.use {
            it.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS).use { stm ->
                // pasarle los parametros
                stm.setString(1, entity.uuid.toString())
                stm.setString(2, entity.marca)
                stm.setString(3, entity.modelo)
                stm.setDouble(4, entity.precio)
                stm.setInt(5, entity.stock)
                stm.setString(6, createdTime.toString())
                stm.setString(7, createdTime.toString())
                stm.setBoolean(8, entity.deleted)

                stm.executeUpdate()

                // Obtengo la clave porque es autoincremental
                val claves = stm.generatedKeys
                if (claves.next()) {
                    myId = claves.getLong(1)
                }
            }
        }

        return entity.copy(id = myId, createdAt = createdTime, updatedAt = createdTime)
    }

    private fun update(entity: Guitarra): Guitarra {
        logger.debug { "update" }

        val updatedTime = LocalDateTime.now()

        val sql = """
            UPDATE guitarras SET marca = ?, modelo = ?, precio = ?, stock = ?, updated_at = ?, deleted = ? WHERE id = ?
        """.trimIndent()
        DataBaseService.db.use {
            it.prepareStatement(sql).use { stm ->
                // pasarle los parametros
                stm.setString(1, entity.marca)
                stm.setString(2, entity.modelo)
                stm.setDouble(3, entity.precio)
                stm.setInt(4, entity.stock)
                stm.setString(5, updatedTime.toString())
                stm.setBoolean(6, entity.deleted)
                stm.setLong(7, entity.id)

                stm.executeUpdate()
            }
        }

        return entity.copy(updatedAt = updatedTime)
    }

    override fun deleteById(id: Long): Boolean {
        logger.debug { "deleteById" }

        var res = 0

        val sql = "DELETE FROM guitarras WHERE id = ?"
        DataBaseService.db.use {
            it.prepareStatement(sql).use { stm ->
                // pasarle los parametros
                stm.setLong(1, id)

                res = stm.executeUpdate()
            }
        }

        return res == 1
    }

    override fun delete(entity: Guitarra): Boolean {
        return deleteById(entity.id)
    }

    override fun deleteAll() {
        logger.debug { "deleteAll" }

        var res = 0

        val sql = "DELETE FROM guitarras"
        DataBaseService.db.use {
            it.prepareStatement(sql).use { stm ->
                res = stm.executeUpdate()
            }
        }
    }

    override fun saveAll(entities: Iterable<Guitarra>) {
        logger.debug { "saveAll" }

        entities.forEach { save(it) }
    }

    override fun count(): Long {
        logger.debug { "count" }

        return findAll().count().toLong()
    }

    override fun findByUuid(uuid: UUID): Guitarra? {
        logger.debug { "findByUuid" }

        var item: Guitarra? = null

        val sql = "SELECT * FROM guitarras WHERE uuid = ?"
        DataBaseService.db.use {
            it.prepareStatement(sql).use { stm ->
                // Le paso los parametros
                stm.setString(1, uuid.toString())

                val rs = stm.executeQuery()

                if (rs.next()) {
                    item = Guitarra(
                        id = rs.getLong("id"),
                        uuid = UUID.fromString(rs.getString("uuid")),
                        marca = rs.getString("marca"),
                        modelo = rs.getString("modelo"),
                        precio = rs.getDouble("precio"),
                        stock = rs.getInt("stock"),
                        createdAt = LocalDateTime.parse(rs.getString("created_at")),
                        updatedAt = LocalDateTime.parse(rs.getString("updated_at")),
                        deleted = rs.getBoolean("deleted")
                    )
                }
            }
        }
        return item
    }

    override fun findByMarca(marca: String): Iterable<Guitarra> {
        logger.debug { "findByMarca" }

        val items = mutableListOf<Guitarra>()

        val sql = "SELECT * FROM guitarras WHERE marca = ?"
        DataBaseService.db.use {
            it.prepareStatement(sql).use { stm ->
                // Le paso los parametros
                stm.setString(1, marca)

                val rs = stm.executeQuery()

                while (rs.next()) {
                    items.add(
                        Guitarra(
                            id = rs.getLong("id"),
                            uuid = UUID.fromString(rs.getString("uuid")),
                            marca = rs.getString("marca"),
                            modelo = rs.getString("modelo"),
                            precio = rs.getDouble("precio"),
                            stock = rs.getInt("stock"),
                            createdAt = LocalDateTime.parse(rs.getString("created_at")),
                            updatedAt = LocalDateTime.parse(rs.getString("updated_at")),
                            deleted = rs.getBoolean("deleted")
                        )
                    )
                }
            }
        }
        return items
    }
}