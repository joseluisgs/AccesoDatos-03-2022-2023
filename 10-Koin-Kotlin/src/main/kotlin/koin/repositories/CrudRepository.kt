package koin.repositories

import koin.models.Persona

/**
 * Interface for CRUD operations on a repository for the type [T] con la clave [ID]
 */
interface CrudRepository<T, ID> {
    fun findAll(): List<T>
    fun findById(id: ID): T?
    fun save(item: Persona): T
    fun delete(item: Persona): T
    fun deleteById(id: ID): T?
    fun deleteAll(): Boolean
}