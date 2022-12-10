package koin.services

/**
 * Interfaz para parsear objetos de tipo [T] a [String] y viceversa usando el formato [F]
 * @param T Tipo de objeto a parsear
 * @param F Formato de parseo
 */
interface Parser<T, E> {
    fun encodeToString(value: List<T>): String
    fun encodeToString(value: T): String
    fun decodeFromString(string: String): List<T>
}