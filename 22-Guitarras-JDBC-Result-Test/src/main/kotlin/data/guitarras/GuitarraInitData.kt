package data.guitarras

import models.Guitarra
import java.util.*

fun getGuitarraInitData() = listOf<Guitarra>(
    Guitarra(
        uuid = UUID.fromString("d8e41c5e-e3ed-46a9-b8d9-16ddf6a6c5d8"),
        marca = "Gibson",
        modelo = "Les Paul Standard",
        precio = 2333.0,
        stock = 10
    ),
    Guitarra(
        uuid = UUID.fromString("f975c0ac-ca3b-4733-ad83-133b500d2c89"),
        marca = "Gibson",
        modelo = "E-335",
        precio = 3250.0,
        stock = 5
    ),
    Guitarra(
        uuid = UUID.fromString("7a8c95f7-a05b-4dd5-9471-08a0efc6e369"),
        marca = "Fender",
        modelo = "Stratocaster",
        precio = 1890.0,
        stock = 15
    ),
    Guitarra(
        uuid = UUID.fromString("223d7e81-210a-4b22-9f4b-58bcf09faf95"),
        marca = "Fender",
        modelo = "Telecaster",
        precio = 1900.0,
        stock = 10
    )
)