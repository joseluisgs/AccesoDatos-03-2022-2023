package es.joseluisgs.encordadosspringdatareactivekotlin.repositories.tenista

import es.joseluisgs.encordadosspringdatareactivekotlin.entities.TenistaEntity
import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository


@Repository
interface TenistasRepository : CoroutineCrudRepository<TenistaEntity, Long> {
    fun findByNombre(nombre: String): Flow<TenistaEntity>
}