plugins {
    java
    id("xyz.jpenilla.run-paper") version "2.1.0"
}

group = "shateq.bukkit"
version = "1.0.0-SNAPSHOT"
description = "Go uphill on ice. Slide your boat as Polish as possible on your Minecraft server. Category: fun"

base {
    archivesName.set("polish-boat-driving-bukkit")
}

repositories {
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20-R0.1-SNAPSHOT")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

tasks {
    compileJava {
        options.encoding = Charsets.UTF_8.name()
    }
    runServer {
        minecraftVersion("1.20")
    }
    processResources {
        filteringCharset = Charsets.UTF_8.name()
        // Token replacing
        val props = mapOf(
            "version" to version,
            "desc" to project.description
        )
        inputs.properties(props)
        filesMatching("plugin.yml") {
            expand(props)
        }
    }
    jar {
        from("LICENSE") {
            rename { "LICENSE_${rootProject.name}" }
        }
    }
}
