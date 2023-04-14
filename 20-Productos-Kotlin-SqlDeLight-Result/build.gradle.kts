plugins {
    kotlin("jvm") version "1.8.0"
    application
    // Plugin de SqlDeLight
    id("app.cash.sqldelight") version "2.0.0-alpha05"
}

group = "dev.joseluisgs"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // Logger
    implementation("ch.qos.logback:logback-classic:1.4.5")
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.4")

    // SqlDeLight para SQLite
    implementation("app.cash.sqldelight:sqlite-driver:2.0.0-alpha05")

    // Result Simple
    // implementation("io.getstream:stream-result:1.1.0")
    // Result Errors
    implementation("com.michael-bull.kotlin-result:kotlin-result:1.1.17")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(11)
}

application {
    mainClass.set("MainKt")
}

// Configuración del plugin de SqlDeLight
sqldelight {
    databases {
        // Nombre de la base de datos
        create("AppDatabase") {
            // Paquete donde se generan las clases
            packageName.set("dev.joseluisgs.database")
        }
    }
}