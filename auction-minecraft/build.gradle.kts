plugins {
    kotlin("jvm") version "1.7.10"
}

repositories {
    papermc()
}

dependencies {
    implementation(kotlin("stdlib"))

    compileOnly(`paper-api`("1.19.2-R0.1-SNAPSHOT"))
    implementation(`kommand-api`("2.14.0"))
}