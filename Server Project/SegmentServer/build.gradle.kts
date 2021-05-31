import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.0"
    kotlin("plugin.jpa") version "1.3.61"
    application
}

group = "me.tiger"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    testImplementation(kotlin("test-junit"))
    compile("org.springframework:spring-jdbc:+")
    compile("commons-dbcp:commons-dbcp:1.4")
    compile("mysql:mysql-connector-java:5.0.8")
    //implementation("org.springframework.boot:spring-boot-starter-data-jpa") // <-- 추가됨
    //implementation("org.springframework.boot:spring-boot-starter-jdbc")
    //implementation("mysql:mysql-connector-java")
    implementation(kotlin("stdlib-jdk8"))
}

tasks.test {
    useJUnit()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClassName = "MainKt"
}
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}