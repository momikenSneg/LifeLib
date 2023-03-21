import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.7.9"
    id("io.spring.dependency-management") version "1.0.15.RELEASE"
    kotlin("jvm") version "1.7.10"
    `maven-publish`
    application
}

group = "ru.momik.life"
version = "1.0"

repositories {
    mavenCentral()
    mavenLocal()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "ru.momik.life"
            artifactId = "lib"
            version = "1.0"

            from(components["java"])
        }
    }
}

dependencies {
    implementation("com.squareup:kotlinpoet:1.12.0")
    implementation("com.squareup:kotlinpoet-metadata:1.12.0")
    implementation("com.squareup:kotlinpoet-metadata-specs:1.9.0")
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "16"
    }
}

application {
    mainClass.set("MainKt")
}