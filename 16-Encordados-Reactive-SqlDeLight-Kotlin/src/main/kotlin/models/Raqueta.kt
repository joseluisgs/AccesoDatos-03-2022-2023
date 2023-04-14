package models

import extensions.toLocalMoney
import kotlinx.serialization.Serializable
import serializers.UUIDSerializer

@Serializable // por lo lo queremos serializar/deseralizar en JSON
data class Raqueta(
    @Serializable(with = UUIDSerializer::class)
    val id: Long = 0,
    val marca: String,
    val precio: Double,
    // val tenistas: List<Tenista>? = null
) {
    override fun toString(): String {
        return "Raqueta(id=$id, marca='$marca', precio='${precio.toLocalMoney()}')"
    }

}


// No voy a guardar para las raquetas los tenistas, porque eso lo puedo consultar de otras maneras ...
// Si no tendríamos un problema de recursividad... Ahora no lo ves, pero seguro que en Acceso a Datos lo ves!!