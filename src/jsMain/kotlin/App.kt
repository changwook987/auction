import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import react.*
import react.dom.html.ReactHTML.h1
import react.dom.html.ReactHTML.li
import react.dom.html.ReactHTML.ul
import kotlin.random.Random

val scope = MainScope()

val App = FC<Props> {
    var users by useState(emptyList<User>())
    useEffectOnce {
        scope.launch {
            users = getUserList()
        }
    }
    h1 { +"Users" }
    ul {
        for (user in users) {
            li {
                key = user.toString()
                +"[${user.nickname} ${user.password}]"
                onClick = {
                    scope.launch {
                        deleteUser(user)
                        users = getUserList()
                    }
                }
            }
        }
    }
    userInput {
        onSubmit = { nickname ->
            scope.launch {
                val password = Random.nextBytes(128).joinToString("") { it.toString(16) }
                val user = User(nickname, password)
                postUser(user)
                users = getUserList()
            }
        }
    }
}