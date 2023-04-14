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
    testImplementation(kotlin("test"))

    // Logger
    implementation("ch.qos.logback:logback-classic:1.4.5")
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.4")

    // Driver de SQLite
    implementation("org.xerial:sqlite-jdbc:3.41.2.1")

    // Moshi
    implementation("com.squareup.moshi:moshi-kotlin:1.14.0")
    implementation("com.squareup.moshi:moshi-adapters:1.14.0")

    // Result un poco más profesional con tus propios errores de dominio
    implementation("com.michael-bull.kotlin-result:kotlin-result:1.1.17")

    // Mockk
    testImplementation("io.mockk:mockk:1.13.5")

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