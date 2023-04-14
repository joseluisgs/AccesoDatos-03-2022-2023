package es.joseluisgs.p06encordadosspringdatakotlin

import es.joseluisgs.p06encordadosspringdatakotlin.controllers.MutuaController
import es.joseluisgs.p06encordadosspringdatakotlin.db.getRaquetasInit
import es.joseluisgs.p06encordadosspringdatakotlin.db.getRepresentantesInit
import es.joseluisgs.p06encordadosspringdatakotlin.db.getTenistasInit
import es.joseluisgs.p06encordadosspringdatakotlin.extensions.toLocalMoney
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class P06EncordadosSpringDataKotlinApplication
constructor(
    @Autowired private val controller: MutuaController
) : CommandLineRunner {

    override fun run(vararg args: String?) {

        println("\uD83C\uDFBE Mutua Madrid Open - Gestión de Tenistas \uD83C\uDFBE")

        // Vamos a hacer el CRUD de Raquetas
        val represetantesInit = getRepresentantesInit()

        val raquetasInit = getRaquetasInit()

        // Asignamos los representantes a las raquetas
        raquetasInit.forEachIndexed { index, raqueta ->
            raqueta.representante = represetantesInit[index]
        }


        // Insertamos
        raquetasInit.forEach { raqueta ->
            controller.createRaqueta(raqueta)
        }
        // Obtenemos todas las raquetas ordenadas por marca
        var raquetas = controller.getRaquetas().sortedBy { it.marca }
        raquetas.forEach { println(it) }

        var tenistas = getTenistasInit()
        tenistas[0].raqueta = raquetas[0] // Nadal, Babolat
        tenistas[1].raqueta = raquetas[2] // Federer, Wilson !! Están ordenadas!!
        tenistas[2].raqueta = raquetas[1] // Djokovic, Head
        tenistas[3].raqueta = raquetas[0] // Thiem, Babolat
        tenistas[4].raqueta = raquetas[0] // Alcaraz, Babolat

        // Insertamos los tenistas
        tenistas.forEach { tenista ->
            controller.createTenista(tenista)
        }

        // Obtenemos todos los tenistas
        tenistas = controller.getTenistas()
        tenistas.forEach { println(it) }

        // Vamos a buscar a un tenista por su uuid
        var tenista = controller.getTenistaById(tenistas[4].uuid)
        // Si no es null lo imprimimos, sabemos que no lo es, pero y si no lo encuentra porque el uuid es incorrecto?
        tenista.let { println(it) }

        // Vamos a cambiarle sus ganancias, que Carlos ha ganado el torneo
        tenista.let {
            it.ganancias += 1000000.0
            controller.updateTenista(it)
        }

        // vamos a buscarlo otra vez, para ver los cambios
        controller.getTenistaById(tenistas[4].uuid).let { println(it) }

        // Vamos a borrar a Roger Federer, porque se retira
        val roger = controller.getTenistaById(tenistas[1].uuid)
        roger.let { if (controller.deleteTenista(it)) println("Roger Federer eliminado") }

        // Sacamos todos los tenistas otra vez
        tenistas = controller.getTenistas().sortedBy { it.ranking }
        tenistas.forEach { tenista ->
            println(tenista)
        }

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

        raquetas = controller.getRaquetas()

        raquetas.forEach { raqueta ->
            println(raqueta)
        }


    }
}

fun main(args: Array<String>) {
    runApplication<P06EncordadosSpringDataKotlinApplication>(*args)
}
