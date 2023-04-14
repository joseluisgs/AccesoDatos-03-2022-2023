import java.io.File
import java.nio.file.Files
import java.sql.DriverManager
import java.sql.Statement

object AppConflig {
    lateinit var databaseUrl: String
    lateinit var outputData: String

    init {
        // Leer el las propiedades
        println("Leyendo las propiedades")
        val fileProperties = ClassLoader.getSystemResource("config.properties").file
        val properties = Properties()
        properties.load(FileInputStream(fileProperties))
        databaseUrl = properties.getProperty("database.url", "jdbc:sqlite::memory:")
        outputData = properties.getProperty("output.data", "data")
    }
}

data class User(val id: Long = -1, val name: String)

object UserRepository {
    private val connection = DriverManager.getConnection(AppConflig.databaseUrl)

    init {
        var sql = """CREATE TABLE IF NOT EXISTS users 
        (id INTEGER PRIMARY KEY, name TEXT NOT NULL)
    """

        connection.createStatement().use { statement ->
            statement.executeUpdate(sql).also { println("Table created") }
        }
    }

    fun findAll(): List<User> {
        var sql = "SELECT * FROM users"

        connection.createStatement().use { statement ->
            statement.executeQuery(sql).use { resultSet ->
                val users = mutableListOf<User>()
                while (resultSet.next()) {
                    users.add(
                        User(
                            resultSet.getLong("id"),
                            resultSet.getString("name")
                        )
                    )
                }
                return users
            }
        }
    }

    fun findById(id: Long): User? {
        var sql = "SELECT * FROM users WHERE id = ?"

        connection.prepareStatement(sql).use { statement ->
            // Le paso los parámetros
            statement.setLong(1, id)

            statement.executeQuery().use { resultSet ->
                if (resultSet.next()) {
                    return User(
                        resultSet.getLong("id"),
                        resultSet.getString("name")
                    )
                }
            }
        }
        return null
    }

    fun save(user: User): User {
        var sql = "INSERT INTO users (name) VALUES (?)"
        var myId = -99L

        connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
            .use { statement ->
                // Le paso los parámetros
                statement.setString(1, user.name)
                val res = statement.executeUpdate()
                println("Insertado $res filas")
                // Ahora voy a por la clave generada
                val rs = statement.generatedKeys
                if (rs.next()) {
                    myId = rs.getLong(1)
                }
            }
        return user.copy(id = myId)
    }

    fun update(user: User): User? {
        var sql = "UPDATE users SET name = ? WHERE id = ?"

        connection.prepareStatement(sql).use { statement ->
            // Le paso los parámetros
            statement.setString(1, user.name)
            statement.setLong(2, user.id)
            val res = statement.executeUpdate()
            println("Actualizado $res filas")
            if (res == 1) return user
        }
        return null
    }

    fun deleteById(id: Long): Boolean {
        var sql = "DELETE FROM users WHERE id = ?"

        connection.prepareStatement(sql).use { statement ->
            // Le paso los parámetros
            statement.setLong(1, id)
            val res = statement.executeUpdate()
            println("Borrado $res filas")
            if (res == 1) return true
        }
        return false
    }
}

fun main(args: Array<String>) {
    println(UserRepository.findAll())
    println(UserRepository.findById(1) ?: "No existe el usuario")
    println(UserRepository.save(User(name = "Juan")))
    println(UserRepository.save(User(name = "Pedro")))
    println(UserRepository.save(User(name = "Ana")))
    println(UserRepository.findAll())
    println(UserRepository.findById(1) ?: "No existe el usuario")
    println(UserRepository.update(User(id = 1, name = "Juanito")))
    println(UserRepository.findAll())
    println(UserRepository.update(User(id = -1, name = "Juanito")))
    println(UserRepository.deleteById(1))
    println(UserRepository.findAll())
    println(UserRepository.deleteById(-1))

    val lista = UserRepository.findAll()

    // Escribir en csv los datos
    Files.createDirectories(File(AppConflig.outputData).toPath())
    val file = File("${AppConflig.outputData}/users.csv")

    // Creamos la cabecera
    file.writeText("id,name\n")
    // Añadimos los datos
    lista.forEach { file.appendText("${it.id},${it.name}\n") }

    // Lo leemos de nuevo
    val lista2 = file.readLines().drop(1).map {
        val campos = it.split(",")
        User(campos[0].toLong(), campos[1])
    }
    println(lista2)

}