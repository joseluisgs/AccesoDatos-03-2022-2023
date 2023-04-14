import db.DataBaseManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import repositories.RolesRepositoryImpl
import repositories.UsersRepositoryImpl

fun main(): Unit = runBlocking {
    println("Hola R2DBC :)")

    // Creamos los repositorios y les pasamos el cliente de la base de datos
    val userRepository = UsersRepositoryImpl(DataBaseManager.client)
    val roleRepository = RolesRepositoryImpl(DataBaseManager.client)

    // Para que se ejecute en el contexto de corutinas

    // Creamos las tablas
    roleRepository.createTable()
    userRepository.createTable()

    // Borramos los datos
    userRepository.deleteAll()
    roleRepository.deleteAll()

    // Insertamos los datos de ejemplo
    roleRepository.initData()
    userRepository.initData()

    // Contamos los datos, Ningna llamada es bloqueante
    println("Roles: ${withContext(Dispatchers.IO) { roleRepository.count() }}")
    println("Users: ${withContext(Dispatchers.IO) { userRepository.count() }}")

    launch(Dispatchers.IO) {
        println("Roles (Reactivo):")
        // Es un flujo de datos, es asincrono y reactivo y lo hará ahora!!
        roleRepository.findAll().collect {
            println(it)
        }
    }

    launch(Dispatchers.IO) {
        println("Users (Reactivo):")
        // Es un flujo de datos, es asincrono y reactivo y lo hará ahora!!
        userRepository.findAll().collect {
            println(it)
        }
    }

}


