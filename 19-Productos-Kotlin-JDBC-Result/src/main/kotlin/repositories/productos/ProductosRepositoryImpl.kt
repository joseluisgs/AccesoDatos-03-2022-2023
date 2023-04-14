package repositories.productos

import models.Producto
import mu.KotlinLogging
import services.DataBaseManager
import java.time.LocalDateTime
import java.util.*

private val logger = KotlinLogging.logger {}

class ProductosRepositoryImpl : ProductosRepository {

    override fun findAll(): List<Producto> {
        logger.debug { "findAll" }
        val productos = mutableListOf<Producto>()
        // Abrimos la conexión a la base de datos
        DataBaseManager.use { db ->
            val sql = "SELECT * FROM productos"
            val result = db.select(sql)
            // Recorremos los resultados
            result?.let {
                while (it.next()) {
                    // Creamos un objeto Producto con los datos de la base de datos en referencia a la fila actual del resultset
                    // y la columna en base a su nombre, debemos tener cuidado con los castinhs y tipos en la base de datos
                    val producto = Producto(
                        id = it.getLong("id"),
                        // it.getObject("uuid") as UUID, // Convertimos el UUID a un objeto UUID para poder usarlo, ahora es texto, podemos hacerlo como texto o como objeto
                        uuid = UUID.fromString(it.getObject("uuid").toString()),
                        nombre = it.getString("nombre"),
                        precio = it.getDouble("precio"),
                        cantidad = it.getInt("cantidad"),
                        createdAt = LocalDateTime.parse(it.getObject("created_at").toString()),
                        updatedAt = LocalDateTime.parse(it.getObject("updated_at").toString()),
                        disponible = it.getBoolean("disponible")
                    )
                    // Agregamos el producto a la lista de productos
                    productos.add(producto)
                }
            }
        }
        return productos
    }

    override fun findById(id: Long): Producto? {
        logger.debug { "findById $id" }
        var producto: Producto? = null
        // Abrimos la conexión a la base de datos
        DataBaseManager.use { db ->
            val sql = "SELECT * FROM productos WHERE id = ?"
            val result = db.select(sql, id) // Ejecutamos la consulta
            // Recorremos los resultados
            result?.let {
                while (it.next()) {
                    // Creamos un objeto Producto con los datos de la base de datos en referencia a la fila actual del resultset
                    // y la columna en base a su nombre, debemos tener cuidado con los castinhs y tipos en la base de datos
                    producto = Producto(
                        id = it.getLong("id"),
                        // it.getObject("uuid") as UUID, // Convertimos el UUID a un objeto UUID para poder usarlo, ahora es texto, podemos hacerlo como texto o como objeto
                        uuid = UUID.fromString(it.getObject("uuid").toString()),
                        nombre = it.getString("nombre"),
                        precio = it.getDouble("precio"),
                        cantidad = it.getInt("cantidad"),
                        createdAt = LocalDateTime.parse(it.getObject("created_at").toString()),
                        updatedAt = LocalDateTime.parse(it.getObject("updated_at").toString()),
                        disponible = it.getBoolean("disponible")
                    )
                }
            }
        }
        return producto
    }

    override fun findByUuid(uuid: String): Producto? {
        logger.debug { "findByUuid $uuid" }
        var producto: Producto? = null
        // Abrimos la conexión a la base de datos
        DataBaseManager.use { db ->
            val sql = "SELECT * FROM productos WHERE uuid = ?"
            val result = db.select(sql, uuid) // Ejecutamos la consulta
            // Recorremos los resultados
            result?.let {
                while (it.next()) {
                    // Creamos un objeto Producto con los datos de la base de datos en referencia a la fila actual del resultset
                    // y la columna en base a su nombre, debemos tener cuidado con los castinhs y tipos en la base de datos
                    producto = Producto(
                        id = it.getLong("id"),
                        // it.getObject("uuid") as UUID, // Convertimos el UUID a un objeto UUID para poder usarlo, ahora es texto, podemos hacerlo como texto o como objeto
                        uuid = UUID.fromString(it.getObject("uuid").toString()),
                        nombre = it.getString("nombre"),
                        precio = it.getDouble("precio"),
                        cantidad = it.getInt("cantidad"),
                        createdAt = LocalDateTime.parse(it.getObject("created_at").toString()),
                        updatedAt = LocalDateTime.parse(it.getObject("updated_at").toString()),
                        disponible = it.getBoolean("disponible")
                    )
                }
            }
        }
        return producto
    }

    override fun findByNombre(nombre: String): List<Producto> {
        logger.debug { "findByNombre $nombre" }
        // Si no sabemos montar la consulta con un Like, podemos usar el método filter de la lista sabiendo devolver todo
        // Es decir, con el select all casi lo podemos hacer todo, es encontrar un equilibrio entre oprocesar nosotros las cosas
        // o dejar que la base de datos lo haga
        return findAll().filter { it.nombre.lowercase().contains(nombre.lowercase()) }
    }

    override fun save(entity: Producto): Producto {
        logger.debug { "save $entity" }
        // Abrimos la conexión a la base de datos
        val createdTime = LocalDateTime.now()
        var myId = 0L
        DataBaseManager.use { db ->
            // Para hacer una transacción, usamos el método transaction, con el cual podemos hacer varias operaciones en la base de datos y si todo va bien, se hace commit
            // esto lo he hecho como prueba, pero no es necesario, porque el insertAndGetKey hace un commit
            db.transaction {
                // El id es autoincremental, por lo que no lo tenemos que pasar por eso usamos el método insertAndGetKey, si no usamos el insert
                // ademas se pasa como null el id, porque no lo tenemos, pero si lo tuvieramos, lo pasariamos
                // cuidado de nuevo con la conversión de datos a los tipos de datos de la base de datos
                // si no, pues podemos pasarle al insert los campos que queramos: insert into tabla (campo1, campo2) values (?, ?)
                val sql = "INSERT INTO productos VALUES (null, ?, ?, ?, ?, ?, ?, ?)"
                val result = db.insertAndGetKey(
                    sql,
                    entity.uuid.toString(),
                    entity.nombre,
                    entity.precio,
                    entity.cantidad,
                    createdTime.toString(), // Lo meto yo ahora
                    createdTime.toString(), // Lo meto yo ahora porque inserto
                    entity.disponible
                )
                // Obtenemos el id que nos ha devuelto la base de datos
                myId = result?.getLong(1) ?: 0L
                // Cerramos la conexión a la base de datos
            }
        }
        // Actualizamos el id de la entidad con el id que nos ha devuelto la base de datos y lo devolvemos
        return entity.copy(id = myId, createdAt = createdTime, updatedAt = createdTime)
    }

    override fun update(entity: Producto): Producto {
        logger.debug { "update $entity" }
        // Abrimos la conexión a la base de datos
        val updatedTime = LocalDateTime.now()
        DataBaseManager.use { db ->
            val sql =
                "UPDATE productos SET nombre = ?, precio = ?, cantidad = ?, updated_at = ?, disponible = ? WHERE id = ?"
            val res = db.update(
                sql,
                entity.nombre,
                entity.precio,
                entity.cantidad,
                updatedTime.toString(), // Lo meto yo ahora porque actualizo
                entity.disponible,
                entity.id
            )
            // Cerramos la conexión a la base de datos
        }
        // Actualizamos el updatedAt de la entidad y lo devolvemos
        // println("res = $res") indica el número de filas actualizadas
        return entity.copy(updatedAt = updatedTime)
    }

    override fun deleteById(id: Long): Boolean {
        logger.debug { "deleteById $id" }
        var result: Int
        // Abrimos la conexión a la base de datos
        DataBaseManager.use { db ->
            val sql = "DELETE FROM productos WHERE id = ?"
            result = DataBaseManager.delete(sql, id)
            // Cerramos la conexión a la base de datos
            // println("result = $result")
        }
        return result == 1 // Cero es que no se ha borrado nada, mayor que cero es que se ha borrado e indica el número de filas borradas
    }
}