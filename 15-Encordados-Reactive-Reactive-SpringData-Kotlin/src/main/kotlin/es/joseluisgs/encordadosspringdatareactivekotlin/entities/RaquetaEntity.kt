package es.joseluisgs.encordadosspringdatareactivekotlin.entities

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

// Al día de Hoy con Spring Data lo mejor es usar id autonuméricos
@Table(name = "RAQUETAS")
data class RaquetaEntity(
    
    var id: Long? = null,@Id
    var marca: String,
    var precio: Double
)