package es.joseluisgs.p06encordadosspringdatakotlin.repositories.tenista

import es.joseluisgs.p06encordadosspringdatakotlin.models.Tenista
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface TenistasRepository : JpaRepository<Tenista, UUID> {
    // Hacemos nuestros métodos de consulta
    // https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repository-query-keywords
    fun findByNombre(nombre: String): List<Tenista>

    @Query("SELECT t FROM Tenista t WHERE t.ganancias >= :ganancias and t.añoProfesional >= :añoProfesional")
    fun hagoLoQueQuiera(ganancias: Double, añoProfesional: Int): List<Tenista>

    fun findByGananciasGreaterThanEqualAndAñoProfesionalGreaterThanEqual(ganancias: Double, año: Int): List<Tenista>
}