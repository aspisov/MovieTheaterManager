import java.io.*
import java.math.BigInteger
import java.security.MessageDigest
import java.security.SecureRandom
import java.util.*

class RegisterLoginManager {
    private val storageFile = File("data/userdata.txt")
    private var users = loadUsers() // load users at the start

    private fun saveUsers(users: Map<String, User>) {
        ObjectOutputStream(FileOutputStream(storageFile)).use { it.writeObject(users) }
    }

    private fun loadUsers(): MutableMap<String, User> {
        if (!storageFile.exists()) return mutableMapOf() // no data yet
        ObjectInputStream(FileInputStream(storageFile)).use { return it.readObject() as MutableMap<String, User> }
    }

    private fun generateSalt(): String {
        val random = SecureRandom()
        val bytes = ByteArray(16)
        random.nextBytes(bytes)
        return Base64.getEncoder().encodeToString(bytes)
    }

    private fun hashPassword(input: String, salt: String): String {
        val md = MessageDigest.getInstance("SHA-256")

        // apply the salt and calculate the hash
        val saltedInput = salt + input
        val messageDigest = md.digest(saltedInput.toByteArray())

        val no = BigInteger(1, messageDigest)
        var hashtext = no.toString(16)

        while (hashtext.length < 32) {
            hashtext = "0$hashtext"
        }

        return hashtext
    }

    private fun register(username: String, password: String): Boolean {
        if (username in users) {
            return false
        }

        val salt = generateSalt()
        val hashedPassword = hashPassword(password, salt)
        users[username] = User(username, hashedPassword, salt)

        saveUsers(users)
        return true
    }

    private fun login(username: String, password: String): Boolean {
        val user = users[username] ?: return false // user not found
        val hashedInputPassword = hashPassword(password, user.salt)

        return user.hashedPassword == hashedInputPassword
    }

    private fun signUp() {
        println("Registering a new user.")
        println("Enter username:")
        val username = readln()
        println("Enter password:")
        val password = readln()

        val success = register(username, password)
        if (success) {
            println("Registration successful.")
            val cinema = CinemaManager()
            cinema.startApplication()
        } else {
            println("Registration failed. User might already exist.")
        }
    }

    private fun signIn() {
        println("Logging in.")
        println("Enter username:")
        val username = readln()
        println("Enter password:")
        val password = readln()

        val success = login(username, password)
        if (success) {
            println("Login successful.")
            val cinema = CinemaManager()
            cinema.startApplication()
        } else {
            println("Login failed. Incorrect username or password.")
        }
    }

    fun startApplication() {
        var input: String

        while (true) {
            println("\n--------- Welcome to Movie Theater Management App ---------")
            println("1. Register")
            println("2. Login")
            println("3. Exit")
            println("Choose an option: ")

            input = readln()
            when (input) {
                "1" -> signUp()
                "2" -> signIn()
                "3" -> {
                    println("Exiting...")
                    return
                }

                else -> println("Invalid option, please try again.")
            }
        }
    }
}