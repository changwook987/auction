plugins {
    kotlin("jvm") version "1.6.10"
}

group = "io.github.changwook987"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/")
}

dependencies {
    implementation(kotlin("stdlib"))

    //monun api
    implementation("io.github.monun:invfx-api:3.1.0")
    implementation("io.github.monun:kommand-api:2.8.1")

    //mysql
    implementation("mysql:mysql-connector-java:8.0.28")

    compileOnly("io.papermc.paper:paper-api:1.18.1-R0.1-SNAPSHOT")
}

project.extra["packageName"] = project.name.replace("-", "")
project.extra["pluginName"] = project.name.split("-").joinToString("") { it.capitalize() }

tasks {
    processResources {
        filesMatching("**/*.yml") {
            expand(project.properties)
            expand(project.extra.properties)
        }
    }

    create<Jar>("paper") {
        from(sourceSets["main"].output)
        archiveBaseName.set(project.extra.properties["pluginName"].toString())
        archiveVersion.set("")

        doLast {
            copy {
                from(archiveFile)
                into(File(rootDir, "server/plugins"))
            }
        }
    }
}