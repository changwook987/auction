import kotlinx.serialization.Serializable

@Serializable
data class User(
    val nickname: String,
    val password: String
) {
    val id = nickname.hashCode()
    override fun hashCode(): Int = id
}