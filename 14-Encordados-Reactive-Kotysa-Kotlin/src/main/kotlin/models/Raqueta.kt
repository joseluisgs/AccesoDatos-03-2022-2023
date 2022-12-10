package models

import extensions.toLocalMoney
import kotlinx.serialization.Serializable
import serializers.UUIDSerializer
import java.util.*

/**
 * Modelo
 * CUIDADO con los tipos d datos que faltan, por ejemplo faltaba en esta version el DOUBLE
 * https://ufoss.org/kotysa/table-mapping.html#h2
 * Lo ideal seria un DTD que luego cambie los datos a lo que quiero, peor no voy a liarlo más
 */


@Serializable // por lo lo queremos serializar/deseralizar en JSON
data class Raqueta(
    @Serializable(with = UUIDSerializer::class)
    val uuid: UUID = UUID.randomUUID(),
    var marca: String,
    var precio: Double,
    // val tenistas: List<Tenista>? = null
) {
    override fun toString(): String {
        return "Raqueta(uuid=$uuid, marca='$marca', precio='${precio.toLocalMoney()}')"
    }
}


// No voy a guardar para las raquetas los tenistas, porque eso lo puedo consultar de otras maneras ...
// Si no tendríamos un problema de recursividad... Ahora no lo ves, pero seguro que en Acceso a Datos lo ves!!