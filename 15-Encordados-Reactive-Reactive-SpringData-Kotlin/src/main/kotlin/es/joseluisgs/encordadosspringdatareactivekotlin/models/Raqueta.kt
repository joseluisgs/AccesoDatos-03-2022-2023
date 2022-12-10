package es.joseluisgs.encordadosspringdatareactivekotlin.models

// Al día de Hoy con Spring Data r2dbc lo mejor es usar id autonuméricos
data class Raqueta(
    var id: Long? = null,
    var marca: String,
    var precio: Double,
)