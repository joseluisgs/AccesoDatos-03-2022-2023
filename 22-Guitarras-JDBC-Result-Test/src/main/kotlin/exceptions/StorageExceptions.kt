package exceptions

sealed class StorageException(mensaje: String) : Exception(mensaje)
class InputStorageException(mensaje: String) : StorageException(mensaje)