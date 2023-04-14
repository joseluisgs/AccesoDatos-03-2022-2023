package repositories.base

interface CrudRepository<T, ID> {
    fun findAll(): Iterable<T>
    fun findById(id: ID): T?
    fun existsById(id: ID): Boolean
    fun save(entity: T): T
    fun deleteById(id: ID): Boolean
    fun delete(entity: T): Boolean
    fun deleteAll()
    fun saveAll(entities: Iterable<T>)
    fun count(): Long
}