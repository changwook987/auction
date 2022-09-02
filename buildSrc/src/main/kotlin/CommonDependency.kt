import org.gradle.kotlin.dsl.DependencyHandlerScope

fun DependencyHandlerScope.jdbc(version: String) = "mysql:mysql-connector-java:$version"