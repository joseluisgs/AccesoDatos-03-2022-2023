package repositories

import kotlinx.coroutines.flow.Flow

// Vamos a simular
// https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/CrudRepository.html
interface CrudRepository<T, ID> {
    fun findAll(): Flow<List<T>> // List<T> es una lista de T
    suspend fun findById(id: ID): T? // nullable puede no existir
    suspend fun save(entity: T): T? // Inserta si no existe, actualiza si existe
    suspend fun delete(entity: T): T? // No es obligatorio el boolean
}