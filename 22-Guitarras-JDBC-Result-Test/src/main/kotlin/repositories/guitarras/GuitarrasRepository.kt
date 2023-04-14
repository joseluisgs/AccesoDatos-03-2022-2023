package repositories.guitarras

import models.Guitarra
import repositories.base.CrudRepository
import java.util.*

interface GuitarrasRepository : CrudRepository<Guitarra, Long> {
    fun findByUuid(uuid: UUID): Guitarra?
    fun findByMarca(marca: String): Iterable<Guitarra>
}