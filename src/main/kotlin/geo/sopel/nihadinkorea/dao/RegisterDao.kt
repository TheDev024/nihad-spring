package geo.sopel.nihadinkorea.dao

data class RegisterDao(
    val username: String,
    val password: String,
    val role: String = "EXTERNAL"
)
