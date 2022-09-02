import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.maven

fun RepositoryHandler.papermc() = maven("https://repo.papermc.io/repository/maven-public/")
fun DependencyHandlerScope.`paper-api`(version: String) = "io.papermc.paper:paper-api:$version"