package repositories.raqueta

import kotlinx.coroutines.flow.Flow
import models.Raqueta
import repositories.CrudRepository

interface RaquetasRepository : CrudRepository<Raqueta, Long> {
    //fun getTenistas(entity: Raqueta): List<Tenista>
    fun findByMarca(marca: String): Flow<List<Raqueta>>
}