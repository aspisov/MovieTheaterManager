import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CinemaManager {
    private val movies = mutableListOf<Movie>()
    private val sessions = mutableListOf<Session>()
    private val dataWriter = SaveFileDataWriter()

    init {
        dataWriter.loadMovies(movies)
        dataWriter.loadSessions(sessions)
    }

    fun startApplication() {
        var input: String
        do {
            println("\nThis is the Main Menu")
            println("Choose a command from below:")
            println("\t1. Show all movies")
            println("\t2. Add new movie")
            println("\t3. Show all sessions")
            println("\t4. Add new session")
            println("\t5. Sell ticket")
            println("\t6. Return ticket")
            println("\t7. Scan ticket")
            println("\tEnter any other input to go back to registration menu.")
            printInputMessage("Only enter an index of chosen operation without any additional symbols:")
            input = readln()

            println("\n------------------")
            when (input) {
                "1" -> printMovies()
                "2" -> addMovie()
                "3" -> printSessions()
                "4" -> addSession()
                "5" -> sellTicket()
                "6" -> returnTicket()
                "7" -> scanTicket()
            }

        } while (input in "1234567" && input != "")
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

        // read duration and manage input errors
        var duration = readMovieDuration()
        while (duration == null) {
            printErrorMessage("Error! Incorrect movie duration, enter an integer.")
            duration = readMovieDuration()
        }

        printInputMessage("Enter the description of this movie in one line:")
        val description = readln()

        val movie = Movie(name, duration, description)
        movies.add(movie)

        dataWriter.saveMovies(movies)
    }

    private fun readMovieDuration(): Int? {
        try {
            printInputMessage("Enter movie duration in minutes:")
            return readln().toInt()
        } catch (e: Throwable) {
            return null
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
                println("Session successfully added.")

                dataWriter.saveSessions(sessions)
            } else {
                printErrorMessage("Error! Session time overlaps with other sessions. Try different time.")
            }
        } catch (e: Throwable) {
            printErrorMessage()
        }
    }

    private fun sellTicket() {
        val data = chooseSessionAndSeat("sell") ?: return
        val (session, seatRow, seatColumn) = data

        println(session.sellTicket(seatRow, seatColumn))

        dataWriter.saveSessions(sessions)
    }

    private fun returnTicket() {
        val data = chooseSessionAndSeat("return") ?: return
        val (session, seatRow, seatColumn) = data

        println(session.returnTicket(seatRow, seatColumn))

        dataWriter.saveSessions(sessions)
    }

    private fun scanTicket() {
        val data = chooseSessionAndSeat("scan") ?: return
        val (session, seatRow, seatColumn) = data

        println(session.scanTicket(seatRow, seatColumn))

        dataWriter.saveSessions(sessions)
    }

    private fun chooseSessionAndSeat(operation: String): Triple<Session, Int, Int>? {
        // print all existing session
        for (i in 0..<sessions.size) {
            println("$i. ${sessions[i]}")
        }

        // read session and manager input errors
        var session = readSession()
        while (session == null) {
            printErrorMessage("Error! This session doesn't seem to exits.")
            session = readSession()
        }

        // managing error that are connected to time depending on the type of desired operation
        if (operation == "scan") {
            if (LocalDateTime.now() < session.startTime.minusHours(1)) {
                printErrorMessage("The ticket can be scanned no earlier than 1 hour before the session.")
                return null
            } else if (LocalDateTime.now() > session.endTime) {
                printErrorMessage("This session has already finished.")
                return null
            }
        } else if (operation == "return") {
            if (LocalDateTime.now() > session.endTime) {
                printErrorMessage("This session has already ended, you cannot return this ticket.")
                return null
            } else if (LocalDateTime.now() > session.startTime) {
                printErrorMessage("This session has already started, you cannot return this ticket")
            }
        } else if (operation == "sell") {
            if (LocalDateTime.now() > session.endTime) {
                printErrorMessage("This session has already ended, you cannot buy this ticket.")
                return null
            }
        }

        session.showAvailableSeats()

        // read seat row and manage input errors
        var seatRow = readRow(session)
        while (seatRow == null) {
            printErrorMessage("Error! This row doesn't seem to exit.")
            seatRow = readRow(session)
        }

        // read seat column and manage input errors
        var seatColumn = readColumn(session)
        while (seatColumn == null) {
            printErrorMessage("Error! This column doesn't seem to exit.")
            seatColumn = readColumn(session)
        }

        return Triple(session, seatRow, seatColumn)
    }

    private fun readSession(): Session? {
        try {
            printInputMessage("Enter an index of chosen session (0-${sessions.size - 1}):")
            val sessionId = readln().toInt()
            return sessions[sessionId]
        } catch (e: Throwable) {
            return null
        }
    }

    private fun readRow(session: Session): Int? {
        try {
            printInputMessage("Enter seat row (0-${session.rows - 1}):")
            val seatRow = readln().toInt()
            if (seatRow < 0 || seatRow > session.rows - 1) {
                return null
            }
            return seatRow
        } catch (e: Throwable) {
            return null
        }
    }

    private fun readColumn(session: Session): Int? {
        try {
            printInputMessage("Enter seat column (0-${session.columns - 1}):")
            val seatColumn = readln().toInt()
            if (seatColumn < 0 || seatColumn > session.columns - 1) {
                return null
            }
            return seatColumn
        } catch (e: Throwable) {
            return null
        }
    }

    private fun canAddThisSessionTime(start: LocalDateTime, end: LocalDateTime): Boolean {
        for (session in sessions) {
            if (start > session.endTime || end < session.startTime) {
                continue
            }
            return false
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
