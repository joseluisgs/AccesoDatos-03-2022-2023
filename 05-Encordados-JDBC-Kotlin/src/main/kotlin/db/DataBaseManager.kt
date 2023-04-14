package db

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import mu.KotlinLogging
import org.apache.ibatis.jdbc.ScriptRunner
import java.io.*
import java.nio.file.Files
import java.nio.file.Paths
import java.sql.*
import java.util.*


/**
 * Manejador de Bases de Datos Relacionales
 * @author Jose Luis González Sánchez
 * @version 1.0
 */

private val logger = KotlinLogging.logger {}

object DataBaseManager : Closeable {
    // Parámetros de conexión
    private lateinit var serverUrl: String
    private lateinit var serverPort: String
    private lateinit var dataBaseName: String
    private lateinit var user: String
    private lateinit var password: String
    private lateinit var initScript: String
    private var poolSize: Int = 0

    /*
    Tipos de Driver
    SQLite: "org.sqlite.JDBC";
    MySQL: "com.mysql.jdbc.Driver"
    MariaDB: "com.mysql.cj.jdbc.Driver"
    PostgreSQL: "org.postgresql.Driver"
    H2: "org.h2.Driver"
    */
    private lateinit var jdbcDriver: String

    //String url = "jdbc:sqlite:"+this.ruta+this.bbdd;
    // MySQL jdbc:mysql://localhost/prueba", "root", "1dam"
    // val url = "jdbc:mariadb://" + serverUrl + ":" + serverPort + "/" + dataBaseName
    // Si lo queremos con un script de inicialización, pero ojo, que lo puede ejecutar siempre!!
    // val url = "jdbc:h2:mem:${this.dataBaseName};INIT=RUNSCRIPT FROM '${this.initScript}'"
    private lateinit var connectionUrl: String

    // Para manejar las conexiones y respuestas de las mismas
    private var connection: Connection? = null
    private var preparedStatement: PreparedStatement? = null

    // HikariCP
    private lateinit var hikariConfig: HikariConfig
    private lateinit var hikariDataSource: HikariDataSource


    init {
        initConfig()
    }

    /**
     * Carga la configuración de acceso al servidor de Base de Datos
     * Puede ser directa "hardcodeada" o asignada dinámicamente a traves de ficheros .env o properties
     */
    private fun initConfig() {
        // Leemos el fichero de configuración
        val propsFile = ClassLoader.getSystemResource("database.properties").file
        propsFile?.let {
            logger.debug { "Cargando fichero de configuración de la Base de Datos: $propsFile" }
            val props = Properties()
            props.load(FileInputStream(propsFile))
            serverUrl = props.getProperty("database.serverUrl", "localhost")
            serverPort = props.getProperty("database.serverPort", "3306")
            dataBaseName = props.getProperty("database.dataBaseName", "tenistas")
            jdbcDriver = props.getProperty("database.jdbcDriver", "org.h2.Driver")
            user = props.getProperty("database.user", "sa")
            password = props.getProperty("database.password", "")
            connectionUrl =
                props.getProperty("database.connectionUrl", "jdbc:h2:mem:${this.dataBaseName};DB_CLOSE_DELAY=-1")
            poolSize = props.getProperty("database.poolSize", "10").toInt()
            initScript = props.getProperty("database.initScript", ClassLoader.getSystemResource("init.sql").file)
        }
        logger.debug { "Configuración de acceso a la Base de Datos cargada" }

        hikariConfig = HikariConfig().apply {
            jdbcUrl = connectionUrl
            driverClassName = jdbcDriver
            username = user
            password = password
            maximumPoolSize = poolSize
        }
        hikariDataSource = HikariDataSource(hikariConfig)
    }

    /**
     * Establece la conexión con la Base de Datos
     * @return true si la conexión se ha establecido correctamente, false en caso contrario
     */
    @Throws(SQLException::class)
    fun open() {

        // Obtenemos la conexión
        connection = hikariDataSource.connection
        logger.debug { "Conexión a la Base de Datos establecida: $connectionUrl" }
    }

    /**
     * Cierra la conexión con el servidor de base de datos
     *
     * @throws SQLException Servidor no responde o no puede realizar la operación de cierre
     */
    @Throws(SQLException::class)
    override fun close() {
        preparedStatement?.close()
        connection?.close()
        logger.debug { "Conexión a la Base de Datos cerrada" }
    }

    /**
     * Realiza una consulta a la base de datos de manera "preparada" obteniendo los parametros opcionales si son necesarios
     *
     * @param querySQL consulta SQL de tipo select
     * @param params   parámetros de la consulta parametrizada
     * @return ResultSet de la consulta
     * @throws SQLException No se ha podido realizar la consulta o la tabla no existe
     */
    @Throws(SQLException::class)
    private fun executeQuery(querySQL: String, vararg params: Any?): ResultSet? {
        this.open()

        preparedStatement = connection?.prepareStatement(querySQL)
        // Si hay parámetros, los asignamos
        return preparedStatement?.let {
            for (i in params.indices) {
                it.setObject(i + 1, params[i])
            }
            logger.debug { "Ejecutando consulta: $querySQL con parámetros: ${params.contentToString()}" }
            it.executeQuery()
        }
    }

    /**
     * Realiza una consulta select a la base de datos de manera "preparada" obteniendo los parametros opcionales si son necesarios
     *
     * @param querySQL consulta SQL de tipo select
     * @param params   parámetros de la consulta parametrizada
     * @return ResultSet de la consulta
     * @throws SQLException No se ha podido realizar la consulta o la tabla no existe
     */
    @Throws(SQLException::class)
    fun select(querySQL: String, vararg params: Any?): ResultSet? {
        return executeQuery(querySQL, *params)
    }

    /**
     * Realiza una consulta select a la base de datos de manera "preparada" obteniendo los parametros opcionales si son necesarios
     *
     * @param querySQL consulta SQL de tipo select
     * @param limit    número de registros de la página
     * @param offset   desplazamiento de registros o número de registros ignorados para comenzar la devolución
     * @param params   parámetros de la consulta parametrizada
     * @return ResultSet de la consulta
     * @throws SQLException No se ha podido realizar la consulta o la tabla no existe o el desplazamiento es mayor que el número de registros
     */
    @Throws(SQLException::class)
    fun select(querySQL: String, limit: Int, offset: Int, vararg params: Any?): ResultSet? {
        val query = "$querySQL LIMIT $limit OFFSET $offset"
        return executeQuery(query, *params)
    }

    /**
     * Realiza una consulta de tipo insert de manera "preparada" con los parametros opcionales si son encesarios
     *
     * @param insertSQL consulta SQL de tipo insert
     * @param params    parámetros de la consulta parametrizada
     * @return Clave del registro insertado si es autonumerico
     * @throws SQLException tabla no existe o no se ha podido realizar la operación
     */
    /* @Throws(SQLException::class)
     fun insert(insertSQL: String, vararg params: Any?): ResultSet? {
         // Con return generated keys obtenemos las claves generadas las claves es autonumerica por ejemplo,
         // el id de la tabla si es autonumérico. Si no quitar.
         preparedStatement = connection?.prepareStatement(insertSQL, *//*Statement.RETURN_GENERATED_KEYS*//*)
        // Si hay parámetros, los asignamos
        return preparedStatement?.let {
            for (i in params.indices) {
                it.setObject(i + 1, params[i])
            }
            logger.info { "Ejecutando consulta: $insertSQL con parámetros: ${params.contentToString()}" }
            it.executeUpdate()
            it.generatedKeys
        }
    }*/

    // USO esta función porque los UUID los genero desde la propia aplicación
    @Throws(SQLException::class)
    fun insert(insertSQL: String, vararg params: Any?): Int {
        return updateQuery(insertSQL, *params)
    }

    /**
     * Realiza una consulta de tipo update de manera "preparada" con los parametros opcionales si son necesarios
     *
     * @param updateSQL consulta SQL de tipo update
     * @param params    parámetros de la consulta parametrizada
     * @return número de registros actualizados
     * @throws SQLException tabla no existe o no se ha podido realizar la operación
     */
    @Throws(SQLException::class)
    fun update(updateSQL: String, vararg params: Any?): Int {
        return updateQuery(updateSQL, *params)
    }

    /**
     * Realiza una consulta de tipo delete de manera "preparada" con los parametros opcionales si son encesarios
     *
     * @param deleteSQL consulta SQL de tipo delete
     * @param params    parámetros de la consulta parametrizada
     * @return número de registros eliminados
     * @throws SQLException tabla no existe o no se ha podido realizar la operación
     */
    @Throws(SQLException::class)
    fun delete(deleteSQL: String, vararg params: Any?): Int {
        return updateQuery(deleteSQL, *params)
    }

    /**
     * Realiza una consulta de tipo update, es decir que modifca los datos, de manera "preparada" con los parametros opcionales si son encesarios
     *
     * @param genericSQL consulta SQL de tipo update, delete, creted, etc.. que modifica los datos
     * @param params     parámetros de la consulta parametrizada
     * @return número de registros eliminados
     * @throws SQLException tabla no existe o no se ha podido realizar la operación
     */
    @Throws(SQLException::class)
    private fun updateQuery(genericSQL: String, vararg params: Any?): Int {
        this.open()

        // Con return generated keys obtenemos las claves generadas
        preparedStatement = connection?.prepareStatement(genericSQL)
        // Si hay parámetros, los asignamos
        return preparedStatement?.let {
            for (i in params.indices) {
                preparedStatement!!.setObject(i + 1, params[i])
            }
            logger.debug { "Ejecutando consulta: $genericSQL con parámetros: ${params.contentToString()}" }
            it.executeUpdate()
        } ?: 0

    }

    fun createTables(genericSQL: String): Int {
        logger.debug { "Creando Tablas..." }
        return updateQuery(genericSQL)
    }

    // Por si es necesario en algunos casos
    /**
     * Inicia una transacción
     *
     * @throws SQLException
     */
    @Throws(SQLException::class)
    private fun beginTransaction() {
        connection?.autoCommit = false
    }

    /**
     * Confirma una transacción
     *
     * @throws SQLException
     */
    @Throws(SQLException::class)
    private fun commit() {
        connection?.commit()
        connection?.autoCommit = true
    }

    /**
     * Cancela una transacción
     *
     * @throws SQLException
     */
    @Throws(SQLException::class)
    private fun rollback() {
        connection?.rollback()
        connection?.autoCommit = true
    }

    /**
     * Realiza unas operaciones en una transacción
     * @param operations operaciones a realizar
     * @throws SQLException si no se ha podido realizar la transacción
     */
    fun transaction(operations: () -> Unit) {
        open()
        try {
            logger.debug { "Transaction iniciada" }
            beginTransaction()
            // Aquí las operaciones
            operations()
            commit()
            logger.debug { "Transaction finalizada" }
        } catch (e: SQLException) {
            rollback()
            logger.error { "Error en la transacción: ${e.message}" }
            throw SQLException(e)
        }
    }

    /**
     * Ejecuta un script que se pasa como
     *
     * @param sqlFile
     * @throws FileNotFoundException
     */
    @Throws(FileNotFoundException::class)
    fun initData(sqlFile: String, logWriter: Boolean = false) {
        this.open()

        check(Files.exists(Paths.get(sqlFile))) { "El fichero $sqlFile no existe" }
        val sr = ScriptRunner(connection) // Si estas con H2, puedes usar RunScript
        val reader: Reader = BufferedReader(FileReader(sqlFile))
        sr.setLogWriter(if (logWriter) PrintWriter(System.out) else null)
        sr.runScript(reader)
    }
}
