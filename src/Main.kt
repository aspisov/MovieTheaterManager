import user_management.*
import movie_theater_management.*
import java.io.File

//fun main() {
//    val dataStorage = JSONDataStorage() // Implement this class based on IDataStorage
//    val movieManager = MovieManager(dataStorage)
//    val sessionManager = SessionManager(dataStorage)
//    val consoleInterface = ConsoleInterface(movieManager, sessionManager)
//
//    consoleInterface.run()
//}

fun main() {
    val userDataRepository = FileUserDataRepository(File("data/userdata.txt"))
    val passwordService = SHA256PasswordService()
    val userService = UserService(userDataRepository, passwordService)
    val userInterface = UserInterface(userService)

    var input: String
    while (true) {
        println("\n--------- Welcome to Movie Theater Management App ---------")
        println("1. Register")
        println("2. Login")
        println("3. Exit")
        println("Choose an option: ")

        input = readln()
        var success = false
        when (input) {
            "1" -> success = userInterface.runSignUp()
            "2" -> success = userInterface.runSignIn()
            "3" -> {
                println("Exiting...")
                return
            }

            else -> println("Invalid option, please try again.")
        }

        if (success) {
            val dataStorage = JSONDataStorage()
            val movieManager = MovieManager(dataStorage)
            val sessionManager = SessionManager(dataStorage)
            val consoleInterface = ConsoleInterface(movieManager, sessionManager)

            consoleInterface.run()
        }
    }
}

