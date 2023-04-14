package repositories

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.Flow
import models.Raqueta

// Vamos a simular
// https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/CrudRepository.html
interface CrudRepository<T, ID> {
    suspend fun findAll(): Flow<T> // List<T> es una lista de T
    suspend fun findById(id: ID): Deferred<T?> // nullable puede no existir
    suspend fun save(entity: T): Deferred<T> // Inserta si no existe, actualiza si existe
    suspend fun delete(entity: T): Deferred<Boolean> // No es obligatorio el boolean
}