import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.20"
    application

    // para serializar Json y otros
    kotlin("plugin.serialization") version "1.7.20"

    // SQLdelight
    id("com.squareup.sqldelight") version "1.5.4"
}

group = "es.joseluisgs"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

    // Para hacer el logging
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.4")
    implementation("ch.qos.logback:logback-classic:1.4.5")

    // Corrutinas
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")

    // Serializa Json con Kotlin
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")

    // SqlDeLight, lo mejor es con SQLite para hacer las cosas reactivas o con corrutinas
    implementation("com.squareup.sqldelight:runtime:1.5.4")
    // SQLite para SqlDeLight
    implementation("com.squareup.sqldelight:sqlite-driver:1.5.4")
    // Para poder usar corrutias en SqlDeLight y conectarnos a la base de datos para cambios
    implementation("com.squareup.sqldelight:coroutines-extensions-jvm:1.5.4")

    // Si quiero usar H2 o similar usar el driver
    implementation("com.h2database:h2:2.1.214")

    // HikkariCP para la conexión a la base de datos con JDBC pero vamos a hacerlo reactivo con SqlDeLight y SQLite
    implementation("com.zaxxer:HikariCP:5.0.1")

}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("MainKt")
}

// Donde vamos a generar el código
buildscript {
    dependencies {
        classpath("com.squareup.sqldelight:gradle-plugin:1.5.4")
    }
}

sqldelight {
    // Debemos colocarlo en el main/sqldelight/database/AppDatabase.sq
    database("AppDatabase") {
        // Como se llama el paquete donde esta el código
        packageName = "database" // Este es el paquete donde se genera el código en sqldelight
        // dialect = "hsql" // Solo para H2
    }
}