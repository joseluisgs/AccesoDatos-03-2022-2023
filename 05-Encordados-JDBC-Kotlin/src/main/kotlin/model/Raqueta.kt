package model

import java.util.*

data class Raqueta(
    val uuid: UUID = UUID.randomUUID(),
    var marca: String,
    var precio: Double
    // val tenistas: List<Tenista>? = null
)