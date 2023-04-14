package sinkoin.repositories

/**
 * Interface for CRUD operations on a repository for the type [T] con la clave [ID]
 */
interface CrudRepository<T, ID> {
    fun findAll(): List<T>
    fun findById(id: ID): T?
    fun save(item: T): T
    fun delete(item: T): T
    fun deleteById(id: ID): T?
    fun deleteAll(): Boolean
}