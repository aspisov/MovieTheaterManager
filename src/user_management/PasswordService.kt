package user_management

interface PasswordService {
    fun generateSalt(): String
    fun hashPassword(password: String, salt: String): String
}

