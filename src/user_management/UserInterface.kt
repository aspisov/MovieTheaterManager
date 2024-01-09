package user_management

class UserInterface(private val userService: UserService) {
    fun runSignUp(): Boolean {
        println("Registering a new user.")
        println("Enter username:")
        val username = readln()
        println("Enter password:")
        val password = readln()

        val success = userService.register(username, password)
        if (success) {
            println("Registration successful.")
            return true
        }
        println("Registration failed. User might already exist.")
        return false
    }

    fun runSignIn(): Boolean {
        println("Logging in.")
        println("Enter username:")
        val username = readln()
        println("Enter password:")
        val password = readln()

        val success = userService.login(username, password)
        if (success) {
            println("Login successful.")
            return true
        }
        println("Login failed. Incorrect username or password.")
        return false
    }
}
