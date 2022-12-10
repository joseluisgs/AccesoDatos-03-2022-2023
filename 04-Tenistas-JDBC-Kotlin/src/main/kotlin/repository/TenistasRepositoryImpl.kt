package repository

import db.DataBaseManager
import model.Tenista
import mu.KotlinLogging
import java.time.LocalDate
import java.util.*

private val logger = KotlinLogging.logger {}

class TenistasRepositoryImpl : TenistasRepository {
    // Lista todos los tenistas
    override fun findAll(): List<Tenista> {
        // Creamos la consulta
        val query = "SELECT * FROM tenistas"
        // Ejecutamos la consulta
        DataBaseManager.open()
        val result = DataBaseManager.select(query)
        // Creamos la lista de tenistas
        val tenistas = mutableListOf<Tenista>()
        // Recorremos el resultado
        result?.let {
            while (result.next()) {
                // Creamos el tenista
                val tenista = Tenista(
                    uuid = it.getObject("uuid") as UUID,
                    nombre = it.getString("nombre"),
                    ranking = result.getInt("ranking"),
                    fechaNacimiento = LocalDate.parse(it.getObject("fecha_nacimiento").toString()),
                    añoProfesional = it.getInt("año_profesional"),
                    altura = it.getInt("altura"),
                    peso = it.getInt("peso"),
                    ganancias = it.getDouble("ganancias"),
                    manoDominante = Tenista.ManoDominante.from(it.getString("mano_dominante")),
                    tipoReves = Tenista.TipoReves.from(it.getString("tipo_reves")),
                    puntos = it.getInt("puntos"),
                    pais = it.getString("pais"),
                )

                // Añadimos el tenista a la lista
                tenistas.add(tenista)
            }
        }

        // Cerramos la conexión
        DataBaseManager.close()
        // Devolvemos la lista de tenistas
        logger.debug { "Tenistas encontrados: ${tenistas.size}" }
        return tenistas.toList()
    }

    override fun findById(id: UUID): Tenista? {
        // Creamos la consulta
        val query = "SELECT * FROM tenistas WHERE uuid = ?"
        // Ejecutamos la consulta
        DataBaseManager.open()
        val result = DataBaseManager.select(query, id)
        // Creamos el tenista
        var tenista: Tenista? = null
        // Recorremos el resultado
        result?.let {
            if (result.next()) {
                // Creamos el tenista
                tenista = Tenista(
                    uuid = it.getObject("uuid") as UUID,
                    nombre = it.getString("nombre"),
                    ranking = result.getInt("ranking"),
                    LocalDate.parse(it.getObject("fecha_nacimiento").toString()),
                    añoProfesional = it.getInt("año_profesional"),
                    altura = it.getInt("altura"),
                    peso = it.getInt("peso"),
                    ganancias = it.getDouble("ganancias"),
                    manoDominante = Tenista.ManoDominante.from(it.getString("mano_dominante")),
                    tipoReves = Tenista.TipoReves.from(it.getString("tipo_reves")),
                    puntos = it.getInt("puntos"),
                    pais = it.getString("pais"),
                )
            }
        }
        DataBaseManager.close()
        // Devolvemos la lista de tenistas
        logger.debug { "Tenista encontrado con $id: $tenista" }
        return tenista
    }

    override fun save(entity: Tenista): Tenista {
        // Si no existe salvamos si existe actualizamos
        // Let run es un if else pero en una sola línea, es más ideomático en Kotlin, no te asustes y usa to if else con null ;)
        val tenista = findById(entity.uuid)
        tenista?.let {
            // Actualizamos
            return update(entity)
        } ?: run {
            // Salvamos
            return insert(entity)
        }
    }

    private fun insert(tenista: Tenista): Tenista {
        // Creamos la consulta
        val query = """INSERT INTO tenistas 
            (uuid, nombre, ranking, fecha_nacimiento, año_profesional, altura, peso, ganancias, mano_dominante, tipo_reves, puntos, pais) 
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"""
            .trimIndent()
        // Ejecutamos la consulta
        DataBaseManager.open()
        val result = DataBaseManager.insert(
            query, tenista.uuid, tenista.nombre, tenista.ranking, tenista.fechaNacimiento,
            tenista.añoProfesional, tenista.altura, tenista.peso, tenista.ganancias, tenista.manoDominante.mano,
            tenista.tipoReves.tipo, tenista.puntos, tenista.pais
        )
        // Cerramos la conexión
        DataBaseManager.close()
        // Devolvemos el tenista
        logger.debug { "Tenista insertado: $tenista - Resultado: ${result == 1}" }
        return tenista
    }

    private fun update(tenista: Tenista): Tenista {
        // Creamos la consulta
        val query = """UPDATE tenistas 
            SET nombre = ?, ranking = ?, fecha_nacimiento = ?, año_profesional = ?, altura = ?, peso = ?, 
            ganancias = ?, mano_dominante = ?, tipo_reves = ?, puntos = ?, pais = ? 
            WHERE uuid = ?"""
            .trimIndent()
        // Ejecutamos la consulta
        DataBaseManager.open()
        val result = DataBaseManager.update(
            query, tenista.nombre, tenista.ranking, tenista.fechaNacimiento,
            tenista.añoProfesional, tenista.altura, tenista.peso, tenista.ganancias, tenista.manoDominante.mano,
            tenista.tipoReves.tipo, tenista.puntos, tenista.pais, tenista.uuid
        )
        // Cerramos la conexión
        DataBaseManager.close()
        // Devolvemos el tenista
        logger.debug { "Tenista actualizado: $tenista - Resultado: ${result == 1}" }
        return tenista
    }

    override fun delete(entity: Tenista): Boolean {
        // Creamos la consulta
        val query = "DELETE FROM tenistas WHERE uuid = ?"
        // Ejecutamos la consulta
        DataBaseManager.open()
        val result = DataBaseManager.delete(query, entity.uuid)
        // Cerramos la conexión
        DataBaseManager.close()
        // Devolvemos el resultado
        logger.debug { "Tenista eliminado: $entity - Resultado: ${result == 1}" }
        return result == 1
    }
}