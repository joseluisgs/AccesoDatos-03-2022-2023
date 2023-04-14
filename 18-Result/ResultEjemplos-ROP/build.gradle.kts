plugins {
    kotlin("jvm") version "1.8.0"
    application
}

group = "dev.joseluisgs"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // Primera librería de Result, sencilla, pero son sus propios errores
    implementation("io.getstream:stream-result:1.1.0")
    // Result un poco más profesional con tus propios errores de dominio
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