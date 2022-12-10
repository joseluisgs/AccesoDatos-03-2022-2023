package es.joseluisgs.encordadosspringdatareactivekotlin

import es.joseluisgs.encordadosspringdatareactivekotlin.controller.MutuaController
import es.joseluisgs.encordadosspringdatareactivekotlin.db.getRaquetasInit
import es.joseluisgs.encordadosspringdatareactivekotlin.db.getTenistasInit
import es.joseluisgs.encordadosspringdatareactivekotlin.extensions.toLocalMoney
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories

@SpringBootApplication
@EnableR2dbcAuditing
@EnableR2dbcRepositories("es.joseluisgs.encordadosspringdatareactivekotlin.repositories")
class EncordadosSpringDataReactiveKotlinApplication
@Autowired constructor(
    private val controller: MutuaController
) : CommandLineRunner {

    override fun run(vararg args: String?): Unit = runBlocking {
        println("\uD83C\uDFBE Mutua Madrid Open - Gestión de Tenistas \uD83C\uDFBE")

        val tenistas = getTenistasInit().toMutableList()
        val raquetas = getRaquetasInit().toMutableList()

        // Creamos las raquetas
        val init = launch {
            raquetas.forEach { controller.createRaqueta(it) }
            // Obtenemos las raquetas
            raquetas.clear()
            controller.getRaquetas().collect { raquetas.add(it) }

            // Asignamos a los tenistas las raquetas
            tenistas[0].raqueta = raquetas[0]
            tenistas[1].raqueta = raquetas[1]
            tenistas[2].raqueta = raquetas[2]
            tenistas[3].raqueta = raquetas[0]
            tenistas[4].raqueta = raquetas[0]

            // Insertamos los tenistas
            tenistas.forEach { controller.createTenista(it) }
            // Obtenemos los tenistas
            tenistas.clear()
            controller.getTenistas().collect { tenistas.add(it) }

            println("Raquetas")
            raquetas.forEach { println(it) }

            println("Tenistas")
            tenistas.forEach { println(it) }
        }

        init.join()

        val auto = launch {
            // Vamos a buscar a un tenista por su id
            println("Buscando a un tenista por su id")
            val tenista = controller.getTenistaById(tenistas[4].id!!)
            // Si no es null lo imprimimos, sabemos que no lo es, pero y si no lo encuentra porque el uuid es incorrecto?
            println(tenista)

            // Vamos a cambiarle sus ganancias, que Carlos ha ganado el torneo
            println("Actualizando las ganancias de un tenista")
            tenista.ganancias += 1000000.0
            controller.updateTenista(tenista)
            println(tenista)

            // comprobamos que se ha actualizado
            println("Comprobamos que se ha actualizado")
            val tenista2 = controller.getTenistaById(tenista.id!!)
            println(tenista2)

            // Vamos a borrar a Roger Federer, porque se retira
            val roger = controller.getTenistaById(tenistas[1].id!!)
            roger.let {
                controller.deleteTenista(it)
                println("Roger Federer eliminado")
            }

            // Sacamos todos los tenistas otra vez
            tenistas.clear()
            controller.getTenistas().collect {
                // println("Tenista: $it")
                tenistas.add(it)
            }
            println("Tenistas ordenados por ranking")
            tenistas.sortedBy { it.ranking }.forEach { println(it) }

        }
        auto.join()

        // Ademas podemos jugar con los tenistas
        // Tenista que más ha ganado
        println()

        println("Tenista con mas ganancias:  ${tenistas.maxByOrNull { it.ganancias }}")
        // Tenista más novel en el circuito
        println("Tenista más novel: ${tenistas.maxByOrNull { it.añoProfesional }}")
        // Tenista más veterano en el circuito
        println("Tenista más veterano: ${tenistas.minByOrNull { it.añoProfesional }}")
        // Tenista más alto
        println("Tenista más alto: ${tenistas.maxByOrNull { it.altura }}")
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
        println("Raqueta más cara: ${raquetas.maxByOrNull { it.precio }}")
        // ¿Qué tenista usa la raqueta más cara?
        println("Tenista con la raqueta más cara: ${tenistas.maxByOrNull { it.raqueta?.precio ?: 0.0 }}")
        // Ganancias totales de todos los tenistas
        println("Ganancias totales: ${tenistas.sumOf { it.ganancias }.toLocalMoney()}")
        // Precio medio de las raquetas
        println("Precio medio de las raquetas: ${raquetas.map { it.precio }.average().toLocalMoney()}")

    }
}

fun main(args: Array<String>) {
    runApplication<EncordadosSpringDataReactiveKotlinApplication>(*args)
}
