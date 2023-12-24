import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm") version "1.9.21"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "org.example"
version = "1.0.0-SNAPSHOT"

repositories {
    maven {
        name = "AiYo Studio Repository"
        url = uri("https://repo.mc9y.com/snapshots")
    }
    mavenCentral()
}

dependencies {
    implementation(fileTree("libs") { include("*.jar") })
    implementation("org.spigotmc:spigot:1.12.2-R0.1-SNAPSHOT")
    implementation("org.spigotmc:spigot-api:1.12.2-R0.1-SNAPSHOT")
    implementation("com.aystudio.core:AyCore:1.0.6-BETA")
}

kotlin {
    jvmToolchain(8)
}

tasks {
    processResources {
        filesMatching("**/plugin.yml") {
            expand("version" to project.version)
        }
    }
    shadowJar {
        archiveFileName = "AnimationControl-$version.jar"
        relocate("kotlin", "kotlin1520")
        exclude { !it.file.toString().contains("AnimationControl\\build") }
        dependsOn(build)
    }
}