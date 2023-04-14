import controllers.MutuaController
import db.SqlDeLightClient
import db.getRaquetasInit
import db.getTenistasInit
import extensions.toLocalMoney
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import models.Raqueta
import models.Tenista
import repositories.raqueta.RaquetasRepositoryImpl
import repositories.tenista.TenistasRepositoryImpl

fun main(args: Array<String>) = runBlocking<Unit> {
    println("\uD83C\uDFBE Mutua Madrid Open - Gestión de Tenistas \uD83C\uDFBE")

    // Inicializamos la base de datos
    SqlDeLightClient.removeAllData()

    // Creamos nuestro controlador y le añadimos y le inyectamos las dependencias
    val controller = MutuaController(
        RaquetasRepositoryImpl(SqlDeLightClient),
        TenistasRepositoryImpl(SqlDeLightClient)
    )

    val raquetas: MutableList<Raqueta> = mutableListOf()
    val tenistas: MutableList<Tenista> = mutableListOf()

    // creamos una corrutina que va a estar ejecutando el método getRaquetas() del controlador
    // y cada vez que obtenga un nuevo valor, lo va a imprimir por pantalla
    val raquetasListener = launch {
        controller.getRaquetas()
            .onStart { println("Obteniendo raquetas...") }
            .onEach {
                println("Obteniendo raquetas actualizadas: $it")
                raquetas.clear()
                raquetas.addAll(it)
            }
            .collect { raquetas ->
                println("Raquetas: $raquetas")
            }
    }

    // creamos una corrutina que va a estar ejecutando el método getTenistas() del controlador
    // y cada vez que obtenga un nuevo valor, lo va a imprimir por pantalla
    val tenistasListener = launch {
        controller.getTenistas()
            .onStart { println("Obteniendo tenistas...") }
            .onEach {
                println("Obteniendo tenistas actualizados: $it")
                tenistas.clear()
                tenistas.addAll(it)
            }
            .collect { tenistas ->
                println("Tenistas: $tenistas")
            }
    }

    val init = launch {

        //delay(500)
        val raquetasInit = getRaquetasInit()

        // Insertamos las raquetas
        raquetasInit.forEach { raqueta ->
            controller.createRaqueta(raqueta)
        }

        delay(500) // Para que se hayan insertado las raquetas
        val tenistasInit = getTenistasInit()
        tenistasInit[0].raqueta = raquetas[0] // Nadal, Babolat
        tenistasInit[1].raqueta = raquetas[1] // Federer, Wilson !! Están ordenadas!!
        tenistasInit[2].raqueta = raquetas[2] // Djokovic, Head
        tenistasInit[3].raqueta = raquetas[0] // Thiem, Babolat
        tenistasInit[4].raqueta = raquetas[0] // Alcaraz, Babolat

        // Insertamos los tenistas
        tenistasInit.forEach { tenista ->
            controller.createTenista(tenista)
        }
    }
    init.join()

    val auto = launch {
        // Vamos a buscar a un tenista por su uuid
        val tenista = controller.getTenistaById(tenistas[4].id)
        // Si no es null lo imprimimos, sabemos que no lo es, pero y si no lo encuentra porque el uuid es incorrecto?
        println(tenista)

        // Vamos a cambiarle sus ganancias, que Carlos ha ganado el torneo
        tenista.let {
            it.ganancias += 1000000.0
            controller.updateTenista(it)
        }

        // vamos a buscarlo otra vez, para ver los cambios
        println(controller.getTenistaById(tenistas[4].id))

        // Vamos a borrar a Roger Federer, porque se retira
        val roger = controller.getTenistaById(tenistas[1].id)
        roger.let {
            controller.deleteTenista(it)
            println("Roger Federer eliminado")
        }

        delay(500)
        println("Tenistas ordenados por ranking")
        tenistas.sortedBy { it.ranking }.forEach { tenista ->
            println(tenista)
        }
    }
    auto.join()

    // Ademas podemos jugar con los tenistas
    // Tenista que más ha ganado
    println()

    println("Tenista con mas ganancias:  ${tenistas.maxBy { it.ganancias }}")
    // Tenista más novel en el circuito
    println("Tenista más novel: ${tenistas.maxBy { it.añoProfesional }}")
    // Tenista más veterano en el circuito
    println("Tenista más veterano: ${tenistas.minBy { it.añoProfesional }}")
    // Tenista más alto
    println("Tenista más alto: ${tenistas.maxBy { it.altura }}")
    // Agrupamos por nacionalidad
    println("Tenistas por nacionalidad ${tenistas.groupBy { it.pais }}")
    // Agrupamos por mano hábil
    println("Tenistas por mano hábil: ${tenistas.groupBy { it.manoDominante }}")
    // ¿Cuantos tenistas a un o dos manos hay?
    val manos = tenistas.groupBy { it.manoDominante }
    manos.forEach { (mano, tenistas) ->
        println("Tenistas con $mano: ${tenistas.size}")
    }
    // ¿Cuantos tenistas hay por cada raqueta?
    val tenistasRaquetas = tenistas.groupBy { it.raqueta } // Así de simple!!!
    tenistasRaquetas.forEach { (raqueta, tenistas) ->
        println("Tenistas con ${raqueta?.marca}: ${tenistas.size}")
    }
    // La raqueta más cara
    println("Raqueta más cara: ${raquetas.maxBy { it.precio }}")
    // ¿Qué tenista usa la raqueta más cara?
    println("Tenista con la raqueta más cara: ${tenistas.maxBy { it.raqueta?.precio ?: 0.0 }}")
    // Ganancias totales de todos los tenistas
    println("Ganancias totales: ${tenistas.sumOf { it.ganancias }.toLocalMoney()}")
    // Precio medio de las raquetas
    println("Precio medio de las raquetas: ${raquetas.map { it.precio }.average().toLocalMoney()}")

    raquetasListener.cancel()
    tenistasListener.cancel()

}