plugins {
    java
    `maven-publish`
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