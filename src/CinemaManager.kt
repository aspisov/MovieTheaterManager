import com.google.gson.*
import java.io.File
import com.google.gson.reflect.TypeToken
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CinemaManager {
    private val movies = mutableListOf<Movie>()
    private val sessions = mutableListOf<Session>()

    // Path to your JSON files
    private val moviesFilePath = "data/movies.json"
    private val sessionsFilePath = "data/sessions.json"

    // Custom Serializer
    private val localDateTimeSerializer = JsonSerializer<LocalDateTime> { src, _, _ ->
        JsonPrimitive(src.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
    }

    // Custom Deserializer
    private val localDateTimeDeserializer = JsonDeserializer { json, _, _ ->
        LocalDateTime.parse(json.asJsonPrimitive.asString, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    }

    // Configure Gson
    private val gson: Gson = GsonBuilder()
        .registerTypeAdapter(LocalDateTime::class.java, localDateTimeSerializer)
        .registerTypeAdapter(LocalDateTime::class.java, localDateTimeDeserializer)
        .create()

    init {
        loadMovies()
        loadSessions()
    }

    fun startApplication() {
        var input: String
        do {
            println("Welcome to the Cinema Management System")
            println("\t1. Show all movies")
            println("\t2. Add new movie")
            println("\t3. Show all sessions")
            println("\t4. Add new session")
            println("\t5. Sell ticket")
            println("\t6. Return ticket")
            println("\tPress any other key to exit")
            printInputMessage("Only enter a number of chosen operation, without any additional symbols:")
            input = readln()

            println("\n------------------\n")
            when (input) {
                "1" -> printMovies()
                "2" -> addMovie()
                "3" -> printSessions()
                "4" -> addSession()
                "5" -> sellTicket()
                "6" -> returnTicket()
            }
            println("\n------------------\n")

        } while (input in "123456" && input != "")
    }

    private fun printMovies() {
        if (movies.size == 0) {
            printErrorMessage("You don't have any movies yet.")
        }
        movies.forEach { println(it) }
    }

    private fun addMovie() {
        printInputMessage("Enter movie name:")
        val name = readln()

        try {
            printInputMessage("Enter movie duration in minutes:")
            val duration = readln().toInt()
            val movie = Movie(name, duration)
            movies.add(movie)

            saveMovies()
        } catch (e: Throwable) {
            printErrorMessage()
        }
    }

    private fun printSessions() {
        if (sessions.size == 0) {
            printErrorMessage("You don't have any sessions yet.")
        }
        sessions.forEach { println(it) }
    }

    private fun addSession() {
        try {
            printInputMessage("Choose a movie by entering a number without any additional symbols:")
            movies.forEachIndexed { index, movie -> println("$index: $movie") }
            val movieIndex = readln().toInt()
            val movie = movies[movieIndex]

            printInputMessage("Enter session time in this format ('HH:mm dd.MM.yyyy'):")
            val sessionTime = readln()

            val formatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy")
            val sessionStartTime = LocalDateTime.parse(sessionTime, formatter)
            val sessionEndTime = sessionStartTime.plusMinutes(movie.duration.toLong())

            if (canAddThisSessionTime(sessionStartTime, sessionEndTime)) {
                val session = Session(movie, sessionStartTime, sessionEndTime)
                sessions.add(session)
                saveSessions()
                println("Session successfully added.")
            } else {
                printErrorMessage("Error! Session time overlaps with other sessions. Try different time.")
            }
        } catch (e: Throwable) {
            printErrorMessage()
        }
    }

    private fun sellTicket() {
        // print all existing session
        for (i in 0..<sessions.size) {
            println("$i. ${sessions[i]}")
        }
        try {
            // read session
            printInputMessage("Enter the number associated with chosen session:")
            val sessionId = readln().toInt()
            val session = sessions[sessionId]

            // if session has already started we return
            if (session.startTime < LocalDateTime.now()) {
                printErrorMessage("This session has already started, you cannot purchase a ticket.")
                return
            }

            session.showAvailableSeats()

            // read seat row
            printInputMessage("Enter seat row (0-${session.rows - 1}):")
            val seatRow = readln().toInt()

            // read seat column
            printInputMessage("Enter seat column (0-${session.columns - 1})")
            val seatColumn = readln().toInt()

            session.sellTicket(seatRow, seatColumn)
        } catch (e: Throwable) {
            printErrorMessage()
        }

        saveSessions()
    }

    private fun returnTicket() {
        // print all existing session
        for (i in 0..<sessions.size) {
            println("$i. ${sessions[i]}")
        }
        try {
            // read session
            printInputMessage("Enter the number associated with chosen session:")
            val sessionId = readln().toInt()
            val session = sessions[sessionId]

            // if session has already started we return
            if (session.startTime < LocalDateTime.now()) {
                printErrorMessage("This session has already started, you cannot return a ticket.")
                return
            }

            session.showAvailableSeats()

            // read seat row
            printInputMessage("Enter seat row (0-${session.rows - 1}):")
            val seatRow = readln().toInt()

            // read seat column
            printInputMessage("Enter seat column (0-${session.columns - 1})")
            val seatColumn = readln().toInt()

            session.returnTicket(seatRow, seatColumn)
        } catch (e: Throwable) {
            printErrorMessage()
        }

        saveSessions()
    }

    private fun loadMovies() {
        val file = File(moviesFilePath)
        if (file.exists()) {
            val type = object : TypeToken<List<Movie>>() {}.type
            movies.addAll(gson.fromJson(file.readText(), type))
        }
    }

    private fun saveMovies() {
        val json = gson.toJson(movies)
        File(moviesFilePath).writeText(json)
    }

    private fun loadSessions() {
        val file = File(sessionsFilePath)
        if (file.exists()) {
            val type = object : TypeToken<List<Session>>() {}.type
            sessions.addAll(gson.fromJson(file.readText(), type))
        }
    }

    private fun saveSessions() {
        sessions.sortBy { it.startTime }
        val json = gson.toJson(sessions)
        File(sessionsFilePath).writeText(json)
    }

    private fun canAddThisSessionTime(start: LocalDateTime, end: LocalDateTime): Boolean {
        for (session in sessions) {
            if (start > session.endTime || end < session.startTime) {
                continue
            } else {
                return false
            }
        }
        return true
    }

    private fun printErrorMessage(message: String = "Error! Incorrect input.") {
        println("\u001B[31m$message\u001B[0m")
    }

    private fun printInputMessage(message: String) {
        println("\u001B[33m$message\u001B[0m")
    }
}
