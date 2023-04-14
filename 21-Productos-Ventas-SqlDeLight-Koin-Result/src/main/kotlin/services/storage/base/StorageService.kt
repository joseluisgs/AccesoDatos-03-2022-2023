package services.storage.base

interface StorageService<T> {
    fun saveAll(items: List<T>)
    fun loadAll(): List<T>
}