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
    // Logger
    implementation("ch.qos.logback:logback-classic:1.4.5")
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.4")

    // SQLite
    implementation("org.xerial:sqlite-jdbc:3.41.2.1")

    // H2
    // implementation("com.h2database:h2:2.1.214")

    // MariaDB
    //implementation("org.mariadb.jdbc:mariadb-java-client:2.7.3")

    // MyBatis para scripts SQL y otras utilidades
    implementation("org.mybatis:mybatis:3.5.11")

    // Result
    implementation("com.michael-bull.kotlin-result:kotlin-result:1.1.17")

    // Terminal mejorado
    //implementation("com.github.ajalt.mordant:mordant:2.0.0-beta13")

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