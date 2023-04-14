package repositories.productos

import models.Producto
import repositories.base.CrudRepository

interface ProductosRepository : CrudRepository<Producto, Long> {
    fun findByUuid(uuid: String): Producto?
    fun findByNombre(nombre: String): List<Producto>
}