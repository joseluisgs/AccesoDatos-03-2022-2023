package factories

import models.Producto
import kotlin.random.Random

fun productoRandom(): Producto {
    return Producto(
        nombre = "Producto ${('A'..'Z').random()}",
        precio = Random.nextDouble(1.0, 100.0),
        cantidad = (10..50).random()
    )
}