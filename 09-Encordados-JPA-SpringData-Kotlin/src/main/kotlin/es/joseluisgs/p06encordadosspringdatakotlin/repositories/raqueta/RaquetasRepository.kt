package es.joseluisgs.p06encordadosspringdatakotlin.repositories.raqueta

import es.joseluisgs.p06encordadosspringdatakotlin.models.Raqueta
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*


@Repository
interface RaquetasRepository : JpaRepository<Raqueta, UUID> {
    // Hacemos nuestros métodos de consulta
    // https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repository-query-keywords
    fun findByMarca(marca: String): Raqueta?

    @Query("SELECT r FROM Raqueta r WHERE r.marca = :marca")
    fun hagoLoQueQuiera(marca: String): Raqueta?
}