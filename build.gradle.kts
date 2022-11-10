plugins {
    id("java")
    id("maven-publish")
    id("com.github.johnrengelman.shadow") version "7.1.0"
}

group = "com.desticube.placeholders"
version = "1.0-SNAPSHOT"
description = "DestiCube's main placeholder plugin"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenLocal()
    maven {
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }

    maven {
        url = uri("https://jitpack.io")
    }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.19.2-R0.1-SNAPSHOT")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7")
    compileOnly("net.luckperms:api:5.4")
}

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}

tasks {
    shadowJar {
//        minimize()
        archiveFileName.set("${rootProject.name}-[v${rootProject.version}].jar")

        listOf("com.gamerduck.commons").forEach {
            relocate(it, "${rootProject.group}.commons")
        }
    }

    compileJava {
        options.release.set(17)
//        options.encoding = "UTF-8"
    }
}