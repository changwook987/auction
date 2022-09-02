plugins {
    id("org.springframework.boot") version "2.7.3"
    id("io.spring.dependency-management") version "1.0.13.RELEASE"
    war
    kotlin("jvm") version "1.7.10"
    kotlin("plugin.spring") version "1.7.10"
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(jdbc("8.0.30"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    providedRuntime("org.springframework.boot:spring-boot-starter-tomcat")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}