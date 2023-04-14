package repositories

import db.getRolesInit
import entities.Roles
import kotlinx.coroutines.flow.Flow
import models.Role
import org.ufoss.kotysa.R2dbcSqlClient
import java.util.*

// datos de la base de datos
private val role_user_uuid = UUID.fromString("79e9eb45-2835-49c8-ad3b-c951b591bc7f")
private val role_admin_uuid = UUID.fromString("67d4306e-d99d-4e54-8b1d-5b1e92691a4e")
private val role_puto_amo_uuid = UUID.fromString("c0e5b9a0-5b9a-4b0c-8b9c-8c5c0b9c8b9c")

class RolesRepositoryImpl(private val client: R2dbcSqlClient) {

    // Obtiene la cuenta
    suspend fun count(): Long = client selectCountAllFrom Roles

    // Todos los roless en un Flow, es asincrono y reactivo R2DBC
    fun findAll(): Flow<Role> = client selectAllFrom Roles

    suspend fun deleteAll() = client deleteAllFrom Roles

    suspend fun save(role: Role) = client insert role

    suspend fun createTable() {
        client createTableIfNotExists Roles
    }

    suspend fun initData() {
        getRolesInit().forEach { role ->
            save(role)
        }
    }
}