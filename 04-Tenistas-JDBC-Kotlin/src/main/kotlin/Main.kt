import controller.MutuaController
import db.DataBaseManager
import db.getTenistasInit
import repository.TenistasRepositoryImpl

fun main(args: Array<String>) {
    println("\uD83C\uDFBE Mutua Madrid Open - Gestión de Tenistas \uD83C\uDFBE")

    // iniciamos las tablas de la base de datos
    initDataBase()

    // Creamos nuestro controlador y le añadimos y le inyectamos las dependencias
    val controller = MutuaController(TenistasRepositoryImpl())

    // Vamos a hacer un CRUD de Tenistas
    // Insertamos
    getTenistasInit().forEach { tenista ->
        controller.createTenista(tenista)
    }
    // Obtenemos todos los tenistas y ordenamos por ranking
    var tenistas = controller.getTenistas().sortedBy { it.ranking }
    tenistas.forEach { tenista ->
        println(tenista)
    }
    // Vamos a buscar a un tenista por su uuid
    val tenista = controller.getTenistaById(tenistas[0].uuid)
    // Si no es null lo imprimimos, sabemos que no lo es, pero y si no lo encuentra porque el uuid es incorrecto?
    tenista?.let { println(it) }

    // Ese fragmento es igual a este ventajas de Kotlin :)
    if (tenista != null) {
        println(tenista)
    }

    // Vamos a cambiarle sus ganancias, que Carlos ha ganado el torneo
    tenista?.let {
        it.ganancias += 1000000.0
        controller.updateTenista(it)
    }

    // vamos a buscarlo otra vez, para ver los cambios
    controller.getTenistaById(tenistas[0].uuid)?.let { println(it) }

    // Vamos a borrar a Roger Federer, porque se retira
    val roger = controller.getTenistaById(tenistas[2].uuid)
    roger?.let { if (controller.deleteTenista(it)) println("Roger Federer eliminado") }

    // Sacamos todos los tenistas otra vez
    tenistas = controller.getTenistas().sortedBy { it.ranking }
    tenistas.forEach { tenista ->
        println(tenista)
    }

    // Ademas podemos jugar con los tenistas
    // Tenista que más ha ganado
    println()
    println("Tenista con mas ganancias: ${tenistas.maxBy { it.ganancias }}")
    // Tenista más joven en el circuito
    println("Tenista más joven: ${tenistas.maxBy { it.añoProfesional }}")
    // Tenista más veterano en el circuito
    println("Tenista más veterano: ${tenistas.minBy { it.añoProfesional }}")
    // Tenista más alto
    println("Tenista más alto: ${tenistas.maxBy { it.altura }}")
    // Agrupamos por nacionalidad
    println("Tenistas por nacionalidad: ${tenistas.groupBy { it.pais }}")
    // Agrupamos por mano hábil
    println("Tenistas por mano hábil: ${tenistas.groupBy { it.manoDominante }}")
    // ¿Cuantos tenistas a un o dos manos hay?
    val manos = tenistas.groupBy { it.manoDominante }
    manos.forEach { mano, tenistas ->
        println("Tenistas con $mano: ${tenistas.size}")
    }

}

fun initDataBase() {
    DataBaseManager.open()
    // Si lo estoy haciendo yo desde el código!!!
    // DataBaseManager.createTables(createTables())
    // Si lo hago paśnadole un fichero SQL, podría crear los datos de la base de datos
    val sqlFile = ClassLoader.getSystemResource("init.sql").file
    DataBaseManager.initData(sqlFile)

    DataBaseManager.close()
}
