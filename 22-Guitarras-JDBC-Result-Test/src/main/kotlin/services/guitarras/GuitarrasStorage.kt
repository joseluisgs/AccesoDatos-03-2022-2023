package services.guitarras

import models.Guitarra
import services.base.Storage

interface GuitarrasStorage : Storage<Guitarra> {
    fun exportToCsv(data: List<Guitarra>)
}
