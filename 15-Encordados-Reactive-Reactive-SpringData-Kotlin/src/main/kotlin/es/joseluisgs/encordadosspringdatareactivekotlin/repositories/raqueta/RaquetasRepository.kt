package es.joseluisgs.encordadosspringdatareactivekotlin.repositories.raqueta

import es.joseluisgs.encordadosspringdatareactivekotlin.entities.RaquetaEntity
import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository


@Repository
interface RaquetasRepository : CoroutineCrudRepository<RaquetaEntity, Long> {
    fun findByMarca(marca: String): Flow<RaquetaEntity>
}