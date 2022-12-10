package repository.tenista

import db.DataBaseManager
import model.Tenista
import mu.KotlinLogging
import repository.raqueta.RaquetasRepository
import java.time.LocalDate
import java.util.*

private val logger = KotlinLogging.logger {}

class TenistasRepositoryImpl(
    private val raquetasRepository: RaquetasRepository
) : TenistasRepository {
    // Lista todos los tenistas
    override fun findAll(): List<Tenista> {
        // Creamos la lista de tenistas
        val tenistas = mutableListOf<Tenista>()
        // Creamos la consulta
        val query = "SELECT * FROM tenistas"
        // Ejecutamos la consulta
        DataBaseManager.use { db ->
            val result = db.select(query)
            // Recorremos el resultado
            result?.let {
                while (result.next()) {
                    // su raqueta
                    val miRaquetaUuid = it.getObject("raqueta_uuid") as? UUID
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
                        // La relación con raquetas se hace en el siguiente paso
                        raqueta = miRaquetaUuid?.let { raqueta_uuid ->
                            raquetasRepository.findById(raqueta_uuid)
                        }
                    )
                    // Añadimos el tenista a la lista
                    tenistas.add(tenista)
                }
            }
        }
        // Devolvemos la lista de tenistas
        logger.debug { "Tenistas encontrados: ${tenistas.size}" }
        return tenistas.toList()
    }

    override fun findById(id: UUID): Tenista? {
        // Creamos el tenista
        var tenista: Tenista? = null
        // Creamos la consulta
        val query = "SELECT * FROM tenistas WHERE uuid = ?"
        // Ejecutamos la consulta
        DataBaseManager.use { db ->
            val result = db.select(query, id)
            // Recorremos el resultado
            result?.let {
                if (result.next()) {
                    // su raqueta
                    val miRaquetaUuid = it.getObject("raqueta_uuid") as? UUID
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
                        // La relación con raquetas se hace en el siguiente paso
                        raqueta = miRaquetaUuid?.let { raqueta_uuid ->
                            raquetasRepository.findById(raqueta_uuid)
                        }
                    )
                }
            }
        }
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
        // En este problema la raqueta si puede ser nula, si no no deberíamos permitirlo
        // require(tenista.raqueta != null) { "La raqueta no puede ser nula" }

        var result: Int
        // Creamos la consulta
        val query = """INSERT INTO tenistas 
            (uuid, nombre, ranking, fecha_nacimiento, año_profesional, altura, peso, ganancias, mano_dominante, tipo_reves, puntos, pais, raqueta_uuid) 
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"""
            .trimIndent()
        // Ejecutamos la consulta
        DataBaseManager.use { db ->
            result = db.insert(
                query, tenista.uuid, tenista.nombre, tenista.ranking, tenista.fechaNacimiento,
                tenista.añoProfesional, tenista.altura, tenista.peso, tenista.ganancias, tenista.manoDominante.mano,
                tenista.tipoReves.tipo, tenista.puntos, tenista.pais, tenista.raqueta?.uuid
            )
        }
        // Devolvemos el tenista
        logger.debug { "Tenista insertado: $tenista - Resultado: ${result == 1}" }
        return tenista
    }

    private fun update(tenista: Tenista): Tenista {
        // En este problema la raqueta si puede ser nula, si no no deberíamos permitirlo
        // require(tenista.raqueta != null) { "La raqueta no puede ser nula" }

        var result: Int
        // Creamos la consulta
        val query = """UPDATE tenistas 
            SET nombre = ?, ranking = ?, fecha_nacimiento = ?, año_profesional = ?, altura = ?, peso = ?, 
            ganancias = ?, mano_dominante = ?, tipo_reves = ?, puntos = ?, pais = ?, raqueta_uuid = ? 
            WHERE uuid = ?"""
            .trimIndent()
        // Ejecutamos la consulta
        DataBaseManager.use { db ->
            result = db.update(
                query, tenista.nombre, tenista.ranking, tenista.fechaNacimiento,
                tenista.añoProfesional, tenista.altura, tenista.peso, tenista.ganancias, tenista.manoDominante.mano,
                tenista.tipoReves.tipo, tenista.puntos, tenista.pais, tenista.raqueta?.uuid,
                tenista.uuid
            )
        }
        // Devolvemos el tenista
        logger.debug { "Tenista actualizado: $tenista - Resultado: ${result == 1}" }
        return tenista
    }

    override fun delete(entity: Tenista): Boolean {
        var result: Int
        // Creamos la consulta
        val query = "DELETE FROM tenistas WHERE uuid = ?"
        // Ejecutamos la consulta
        DataBaseManager.use { db ->
            result = db.delete(query, entity.uuid)
            // Cerramos la conexión
        }
        // Devolvemos el resultado
        logger.debug { "Tenista eliminado: $entity - Resultado: ${result == 1}" }
        return result == 1
    }
}