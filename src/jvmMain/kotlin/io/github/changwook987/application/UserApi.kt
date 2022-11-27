package io.github.changwook987.application

import User
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

val userList = ArrayList<User>()

fun Routing.userApi() = route("/api/user") {
    get("/") {
        call.respond(
            HttpStatusCode.OK,
            userList
        )
    }
    post("/") {
        userList += call.receive<User>()
        call.respond(HttpStatusCode.OK)
    }
    delete("/{nickname}") {
        val nickname = call.parameters["nickname"] ?: error("invalid delete request")
        if (userList.removeIf { it.nickname == nickname })
            call.respond(HttpStatusCode.OK)
        else
            call.respond(HttpStatusCode.BadRequest)
    }
}