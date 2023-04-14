package repositories

import db.getUsersInit
import dto.UserDto
import entities.Roles
import entities.Users
import kotlinx.coroutines.flow.Flow
import models.User
import org.ufoss.kotysa.R2dbcSqlClient
import java.util.*

/**
 * Repositorio de usuarios, le paso el cliente de la base de datos
 * Seria la dependencia, por si luego la quiero inyectar!!!
 */

// datos de la base de datos
// datos de la base de datos
private val role_user_uuid = UUID.fromString("79e9eb45-2835-49c8-ad3b-c951b591bc7f")
private val role_admin_uuid = UUID.fromString("67d4306e-d99d-4e54-8b1d-5b1e92691a4e")

class UsersRepositoryImpl(private val client: R2dbcSqlClient) : UsersRepository {

    // Total de usuarios
    override suspend fun count(): Long = client selectCountAllFrom Users

    // Todos los usuarios en un Flow, es asincrono y reactivo R2DBC
    override fun findAll(): Flow<User> = client selectAllFrom Users

    // Un usuario por su id
    override suspend fun findById(id: Int): User = (client selectFrom Users
            where Users.id eq id).fetchOne()
        ?: throw IllegalArgumentException("No existe usuario con $id")

    // Un usuario por su alias
    fun selectWithJoin(): Flow<UserDto> =
        // Construyo la consulta de usuario
        (client.selectAndBuild {
            UserDto("${it[Users.firstname]} ${it[Users.lastname]}", it[Users.alias], it[Roles.label]!!)
        } from Users innerJoin Roles on Users.roleId eq Roles.id
                ).fetchAll()

    // Borramos todos los usuarios
    override suspend fun deleteAll(): Int = client deleteAllFrom Users

    // Salvamos un usuario
    override suspend fun save(user: User): Unit? = client.transactional {
        client insert user
    }

    // Update user
    override suspend fun update(user: User) = client.transactional {
        (client update Users set Users.firstname eq user.firstname
                where Users.id eq user.id!!).execute()
    }

    override suspend fun delete(user: User) = client.transactional {
        (client deleteFrom Users
                where Users.id eq user.id!!).execute()
    }

    suspend fun createTable() {
        client createTableIfNotExists Users
    }

    suspend fun initData() {
        getUsersInit().forEach {
            save(it)
        }
    }
}