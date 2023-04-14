import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    // OJO ajusta la versión de Kotlin a la de Koin y KSP
    kotlin("jvm") version "1.7.21"
    // para serializar Json y otros
    kotlin("plugin.serialization") version "1.7.21"
    // Plugin KSP para generar código en tiempo de compilación
    id("com.google.devtools.ksp") version "1.7.21-1.0.8"
}

group = "es.joseluisgs"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // Koin - Core (con esto ya va Koin en modo normal!!
    implementation("io.insert-koin:koin-core:3.2.2")

    // Si queremos usar Koin en modo Annotations
    implementation("io.insert-koin:koin-annotations:1.0.3")
    ksp("io.insert-koin:koin-ksp-compiler:1.0.3")

    // Para hacer el logging
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.4")
    implementation("ch.qos.logback:logback-classic:1.4.4")

    // Serializa Json
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
    // Serializa a XML con Serialization  para jvm
    // https://github.com/pdvrieze/xmlutil
    implementation("io.github.pdvrieze.xmlutil:core-jvm:0.84.3")
    implementation("io.github.pdvrieze.xmlutil:serialization-jvm:0.84.3")


    // Terminal mejorado: https://github.com/ajalt/mordant
    //implementation("com.github.ajalt.mordant:mordant:2.0.0-beta8")

    // Test integrado, lo quitamos si queremos usar la opción de Koin Test
    //testImplementation(kotlin("test")) // IMPORTANTE: No usar esta opción si queremos usar Koin Test

    // Si queremos usar Mokk para test, es mokito para Kotlin
    implementation("io.mockk:mockk:1.13.2")

    // Si queremos usar Koin en test, no es necesario , porque podemos usar el KoinComponent
    testImplementation("io.insert-koin:koin-test:3.2.2")
    testImplementation("io.insert-koin:koin-test-junit5:3.2.2")
    // Debemos añadir el JUnit 5 y no usar el Junit 5 que ya trae Kotlin, si wuremos Koin Test
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

// Para Koin Annotations, directorio donde se encuentran las clases compiladas
// KSP - To use generated sources
sourceSets.main {
    java.srcDirs("build/generated/ksp/main/kotlin")
}
