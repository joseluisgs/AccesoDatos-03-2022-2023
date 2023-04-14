package services.storage.ventas

import models.Venta
import services.storage.base.StorageService

interface VentasStorageService : StorageService<Venta> {
    fun saveVenta(venta: Venta)
}