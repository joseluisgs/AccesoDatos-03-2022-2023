package repositories

import kotlinx.coroutines.flow.Flow
import models.User

interface CrudRepository<T, ID> {
    suspend fun count(): Long
    fun findAll(): Flow<User>
    suspend fun findById(id: ID): T
    suspend fun deleteAll(): ID
    suspend fun save(user: User): Unit?
    suspend fun update(user: User): ID?
    suspend fun delete(user: User): ID?
}