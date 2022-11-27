import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*

val jsonClient = HttpClient {
    install(ContentNegotiation) {
        json()
    }
}

suspend fun getUserList(): List<User> {
    return jsonClient.get("/api/user/").body()
}

suspend fun postUser(user: User) {
    jsonClient.post("/api/user/") {
        contentType(ContentType.Application.Json)
        setBody(user)
    }
}

suspend fun deleteUser(user: User) {
    jsonClient.delete("/api/user/${user.nickname}")
}