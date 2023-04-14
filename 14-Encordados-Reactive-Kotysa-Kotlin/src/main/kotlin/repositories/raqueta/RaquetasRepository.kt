package repositories.raqueta

import kotlinx.coroutines.flow.Flow
import models.Raqueta
import repositories.CrudRepository
import java.util.*

interface RaquetasRepository : CrudRepository<Raqueta, UUID> {
    //fun getTenistas(entity: Raqueta): List<Tenista>
    fun findByMarca(marca: String): Flow<Raqueta>
}