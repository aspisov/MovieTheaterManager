package user_management

class UserService(
    private val userDataRepository: UserDataRepository,
    private val passwordService: PasswordService
) {
    private var users = userDataRepository.loadUsers()

    fun register(username: String, password: String): Boolean {
        if (username in users) {
            return false
        }

        val salt = passwordService.generateSalt()
        val hashedPassword = passwordService.hashPassword(password, salt)
        users[username] = User(username, hashedPassword, salt)

        userDataRepository.saveUsers(users)
        return true
    }

    fun login(username: String, password: String): Boolean {
        val user = users[username] ?: return false // user not found
        val hashedInputPassword = passwordService.hashPassword(password, user.salt)

        return user.hashedPassword == hashedInputPassword
    }
}
