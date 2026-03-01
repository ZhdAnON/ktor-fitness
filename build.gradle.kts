plugins {
    kotlin("jvm") version "2.0.0"
    id("io.ktor.plugin") version "2.3.8"
    kotlin("plugin.serialization") version "2.0.0"
    application
}

kotlin {
    jvmToolchain(17)
}

group = "com.fitness"
version = "0.0.1"

application {
    mainClass.set("com.zhdanon.ApplicationKt")
}

repositories {
    mavenCentral()
}

dependencies {
    // Ktor
    implementation("io.ktor:ktor-server-core-jvm:2.3.8")
    implementation("io.ktor:ktor-server-netty-jvm:2.3.8")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:2.3.8")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:2.3.8")
    implementation("io.ktor:ktor-server-auth-jvm:2.3.8")
    implementation("io.ktor:ktor-server-auth-jwt-jvm:2.3.8")

    // Serialization + Kotlinx datetime
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.0")
    implementation("org.jetbrains.exposed:exposed-core:...")

    // Exposed + PostgreSQL + Hikari
    implementation("org.jetbrains.exposed:exposed-core:0.50.1")
    implementation("org.jetbrains.exposed:exposed-dao:0.50.1")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.50.1")
    implementation("org.jetbrains.exposed:exposed-kotlin-datetime:0.50.1")
    implementation("com.zaxxer:HikariCP:5.1.0")
    implementation("org.postgresql:postgresql:42.7.3")
    implementation("org.jetbrains.exposed:exposed-json:0.50.1")

    // Logging
    implementation("ch.qos.logback:logback-classic:1.4.14")
    implementation("io.ktor:ktor-server-call-logging-jvm:2.3.8")
    implementation("io.ktor:ktor-server-status-pages-jvm:2.3.8")

    // BCrypt
    implementation("org.mindrot:jbcrypt:0.4")
    implementation(kotlin("stdlib-jdk8"))
    implementation("at.favre.lib:bcrypt:0.10.2")

    // Object Storage s3Client
    implementation("software.amazon.awssdk:s3:2.25.0")
    implementation("software.amazon.awssdk:auth:2.25.0")
}