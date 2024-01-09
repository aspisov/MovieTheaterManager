package user_management

import java.io.Serializable

data class User(val username: String, val hashedPassword: String, val salt: String) : Serializable