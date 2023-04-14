import config.AppConfig
import config.DataBase
import controllers.RaquetasController
import controllers.TenistasController
import models.Raqueta
import models.Tenista
import mu.KotlinLogging
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import repositories.raquetas.RaquetasRepository
import repositories.tenistas.TenistasRepository
import services.raquetas.StorageRaquetasCsvService
import services.raquetas.StorageRaquetasCsvServiceImpl
import services.tenistas.StorageTenistasCsvService
import services.tenistas.StorageTenistasCsvServiceImpl
import services.tenistas.StorageTenistasJsonService
import services.tenistas.StorageTenistasJsonServiceImpl
import utils.toSystemPath
import java.io.File
import java.time.LocalDate

/**
 * Clase de de la Aplicación
 */
private val logger = KotlinLogging.logger {}

const val APP_PROPERTIES = "src/main/resources/config.properties"

const val TENISTAS_INPUT_CSV_FILE = "data/tenistas.csv"
const val RAQUETAS_INPUT_CSV_FILE = "data/raquetas.csv"

const val RAQUETAS_OUTPUT_CSV_FILE = "data/raquetas-output.csv"
const val TENISTAS_OUTPUT_CSV_FILE = "data/tenistas-output.csv"

const val TENISTAS_INPUT_JSON_FILE = "data/tenistas.json"
const val TENISTAS_OUTPUT_JSON_FILE = "data/tenistas-output.json"

class ResumenApp : KoinComponent {
    var appConfig: AppConfig = AppConfig.fromPropertiesFile(File(APP_PROPERTIES.toSystemPath()))

    init {
        logger.debug { "Iniciando la aplicación" }
        logger.debug { "Configuración: $appConfig" }
        // Iniciamos la base de datos, las tablas y todo de acuerdo a nuestra configuracion
        // Por favor mira bien el logger para ver que pasa
        DataBase.init(appConfig)
    }

    /**
     * Método principal de la aplicación
     */
    fun run() {
        println("\uD83D\uDC4B Hola Resumen")
        println("===============")


        // Para que lo veas por partes.
        // jugandoConStorage()

        // Bases de datos con repositorios
        // jugandoConBaseDatos()

        // Jugando con controladores
        jugandoConControladores()
    }

    /**
     * Jugando con controladores que son los que controlan todo
     * Tienen como dependencias el resto de elementos: repositorios y servicios
     * Los controladores son los que se encargan de la lógica de negocio
     * controlan cómo y cúando acceder a las cosas
     * Ya verás adelante en otras arquitecturas como Spring que controlan el punto de acceso a las rutas
     * de una api rest, por ejemplo.../raquetas o /tenistas
     */
    private fun jugandoConControladores() {
        val raquetasController: RaquetasController by inject()

        raquetasController.importDataFromCsv(File(RAQUETAS_INPUT_CSV_FILE.toSystemPath()))
        val raquetas = raquetasController.getAll()
        println("Raquetas: $raquetas")

        var raqueta = Raqueta(
            marca = "Raqueta Controller",
            modelo = "Modelo Controller",
            precio = 100.0,
            peso = 100
        )

        raqueta = raquetasController.save(raqueta)
        println("Raqueta guardada: $raqueta")

        raqueta.apply {
            marca = "Raqueta Controller 2"
            modelo = "Modelo Controller 2"
            precio = 200.0
            peso = 200
        }

        raqueta = raquetasController.save(raqueta)
        println("Raqueta guardada: $raqueta")

        raqueta = raquetasController.getById(raqueta.id)
        println("Raqueta obtenida: $raqueta")

        raqueta = raquetasController.delete(raqueta)
        println("Raqueta eliminada: $raqueta")

        // Lo usaremos...
        val babolatAero = raquetasController.getByMarcaAndModelo("Babolat", "Aero")

        val reportRaquetas = raquetasController.report()
        println("Informe de raquetas: $reportRaquetas")

        val tenistasController: TenistasController by inject()

        tenistasController.importDataFromCsv(File(TENISTAS_INPUT_CSV_FILE.toSystemPath()))
        val tenistas = tenistasController.getAll()
        println("Tenistas: $tenistas")

        var tenista = Tenista(
            nombre = "Tenista Controller",
            ranking = 33,
            fechaNacimiento = LocalDate.parse("1995-06-04"),
            añoProfesional = 2005,
            altura = 185,
            peso = 80,
            ganancias = 10000000.0,
            manoDominante = Tenista.ManoDominante.from("Izquierda"),
            tipoReves = Tenista.TipoReves.from("dos manos"),
            puntos = 6789
        )

        tenista = tenistasController.save(tenista)
        println("Tenista guardado: $tenista")

        tenista.apply {
            nombre = "Tenista Controller 2"
            ranking = 44
            puntos = 7777
        }

        tenista = tenistasController.save(tenista)
        println("Tenista guardado: $tenista")

        tenista = tenistasController.getById(tenista.id)
        println("Tenista obtenido: $tenista")

        tenista = tenistasController.delete(tenista)
        println("Tenista eliminado: $tenista")

        // Vamos a asignarles raquetas
        // Novak
        tenista = tenistasController.getByName("Novak")[0]
        tenista.raqueta = raquetasController.getByMarcaAndModelo("head", "speed")
        tenistasController.save(tenista)
        // Daniil
        tenista = tenistasController.getByName("Daniil")[0]
        tenista.raqueta = raquetasController.getByMarcaAndModelo("tecnifibre", "tfight")
        tenistasController.save(tenista)
        // Zverev
        tenista = tenistasController.getByName("Zverev")[0]
        tenista.raqueta = raquetasController.getByMarcaAndModelo("head", "gravity")
        tenistasController.save(tenista)
        // Nadal
        tenista = tenistasController.getByName("Nadal")[0]
        tenista.raqueta = raquetasController.getByMarcaAndModelo("babolat", "aero")
        tenistasController.save(tenista)
        // Stefanos
        tenista = tenistasController.getByName("Stefanos")[0]
        tenista.raqueta = raquetasController.getByMarcaAndModelo("wilson", "blade")
        tenistasController.save(tenista)
        // Matteo
        tenista = tenistasController.getByName("Matteo")[0]
        tenista.raqueta = raquetasController.getByMarcaAndModelo("head", "extreme")
        tenistasController.save(tenista)
        // Casper
        tenista = tenistasController.getByName("Casper")[0]
        tenista.raqueta = raquetasController.getByMarcaAndModelo("yonex", "ezone")
        tenistasController.save(tenista)
        // Cameron
        tenista = tenistasController.getByName("Cameron")[0]
        tenista.raqueta = raquetasController.getByMarcaAndModelo("babolat", "strike")
        tenistasController.save(tenista)
        // Alcaraz
        tenista = tenistasController.getByName("Alcaraz")[0]
        tenista.raqueta = raquetasController.getByMarcaAndModelo("babolat", "aero")
        tenistasController.save(tenista)
        // Sinner
        tenista = tenistasController.getByName("Sinner")[0]
        tenista.raqueta = raquetasController.getByMarcaAndModelo("head", "speed")
        tenistasController.save(tenista)
        // Felix
        tenista = tenistasController.getByName("Auger")[0]
        tenista.raqueta = raquetasController.getByMarcaAndModelo("babolat", "aero")
        // Rublev
        tenista = tenistasController.getByName("Rublev")[0]
        tenista.raqueta = raquetasController.getByMarcaAndModelo("head", "gravity")
        tenistasController.save(tenista)


        val reportTenistas = tenistasController.report()
        println("Informe de tenistas: $reportTenistas")

        tenistasController.exportDataToJson(File(TENISTAS_OUTPUT_JSON_FILE.toSystemPath()))


    }

    /**
     * Jugando con Bases de Datos y repositorios
     */
    private fun jugandoConBaseDatos() {

        // Repositorios con DI
        val raquetasRepository: RaquetasRepository by inject()
        val tenistasRepository: TenistasRepository by inject()

        // Vamos a crear varias raquetas
        var babolatAero = Raqueta(marca = "Babolat", modelo = "Pure Aero", precio = 195.0, peso = 300)
        var babolatDrive = Raqueta(marca = "Babolat", modelo = "Pure Drive", precio = 185.50, peso = 300)
        var headSpeed = Raqueta(marca = "Head", modelo = "Speed", precio = 205.50, peso = 305)
        var headBoom = Raqueta(marca = "Head", modelo = "Boom", precio = 200.0, peso = 295)


        // Insertamos raquetas
        babolatAero = raquetasRepository.save(babolatAero)
        babolatDrive = raquetasRepository.save(babolatDrive)
        headSpeed = raquetasRepository.save(headSpeed)
        headBoom = raquetasRepository.save(headBoom)

        println("Babolat Aero: $babolatAero")

        // Obtenemos todas las raquetas
        var raquetas = raquetasRepository.findAll()
        println("Raquetas: $raquetas")

        // Actualizamos head boom
        headBoom.apply {
            marca = "Head Boom updated"
            precio = 210.0
        }
        raquetasRepository.save(headBoom)

        // Buscamos head boom
        val headBoomUpdated = raquetasRepository.findById(headBoom.id)
        println("Head Boom updated: $headBoomUpdated")

        // Borramos head boom
        raquetasRepository.delete(headBoom)

        // Obtenemos todas las raquetas
        raquetas = raquetasRepository.findAll()
        println("Raquetas: $raquetas")

        var nadal = Tenista(
            nombre = "Rafael Nadal",
            ranking = 3,
            fechaNacimiento = LocalDate.parse("1985-06-04"),
            añoProfesional = 2005,
            altura = 185,
            peso = 80,
            ganancias = 10000000.0,
            manoDominante = Tenista.ManoDominante.from("Izquierda"),
            tipoReves = Tenista.TipoReves.from("dos manos"),
            puntos = 6789,
            raqueta = babolatAero
        )

        nadal = tenistasRepository.save(nadal)
        println("Tenista nadal: $nadal")

        // tenistas con babolat aero... Esto no es necsario hacerlo, porque podemos hacerlo filtrando....
        /*val tenistas = raquetasRepository.getTenistas(babolatAero)
        println("Tenistas con Babolat Aero: $tenistas")*/

        val tenistasAero = tenistasRepository.findAll()
            .filter { it.raqueta == babolatAero } // También lo podríamos hacer con un método en el repositorio por ejemplo finByRaquet
        println("Tenistas con Babolat Aero: $tenistasAero")

        // Actualizamos nadal
        nadal.apply {
            nombre = "Rafael Nadal updated"
            ranking = 4
            raqueta = babolatDrive
        }

        tenistasRepository.save(nadal)
        println("Tenistas nadal: $nadal")

        nadal.apply {
            raqueta = null
        }

        tenistasRepository.save(nadal)
        println("Tenistas nadal: $nadal")

        // Borramos nadal
        tenistasRepository.delete(nadal)

        // Obtenemos todos los tenistas
        val tenistas = tenistasRepository.findAll()
        println("Tenistas: $tenistas")

    }

    /**
     * Jugando con los servicios de Storage
     */
    private fun jugandoConStorage() {
        val raquetasCsvStorage: StorageRaquetasCsvService = StorageRaquetasCsvServiceImpl()
        val raquetas = raquetasCsvStorage.loadFromFile(File(RAQUETAS_INPUT_CSV_FILE.toSystemPath()))
        println("Raquetas: $raquetas")

        val tenistasCsvStorage: StorageTenistasCsvService = StorageTenistasCsvServiceImpl()
        var tenistas = tenistasCsvStorage.loadFromFile(File(TENISTAS_INPUT_CSV_FILE.toSystemPath()))
        println("Tenistas: $tenistas")

        raquetasCsvStorage.saveToFile(File(RAQUETAS_OUTPUT_CSV_FILE), raquetas.sortedBy { it.marca })
        tenistasCsvStorage.saveToFile(File(TENISTAS_OUTPUT_CSV_FILE), tenistas.sortedBy { it.ranking })

        val tenistasJsonStorage: StorageTenistasJsonService = StorageTenistasJsonServiceImpl()
        tenistasJsonStorage.saveToFile(File(TENISTAS_OUTPUT_JSON_FILE), tenistas.sortedBy { it.ranking })
        tenistas = tenistasJsonStorage.loadFromFile(File(TENISTAS_INPUT_JSON_FILE.toSystemPath()))
        println("Tenistas: $tenistas")
    }
}