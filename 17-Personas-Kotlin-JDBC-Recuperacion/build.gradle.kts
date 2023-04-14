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

    // Driver de SQLite
    implementation("org.xerial:sqlite-jdbc:3.41.2.1")

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