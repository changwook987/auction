import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins { kotlin("jvm") version "1.7.10" }

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

allprojects {
    repositories {
        mavenCentral()
    }

    tasks {
        withType(KotlinCompile::class) {
            kotlinOptions {
                jvmTarget = "17"
            }
        }
    }
}