package movie_theater_management

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class ConsoleInterface(
    private val movieManager: MovieManager,
    private val sessionManager: SessionManager,
) {
    fun run() {
        var input: String
        do {
            displayMainMenu()
            input = readln()

            when (input) {
                "1" -> printAndEditMovies()
                "2" -> addMovie()
                "3" -> printAndEditSessions()
                "4" -> addSession()
                "5" -> sellTicket()
                "6" -> returnTicket()
                "7" -> scanTicket()
            }
        } while (input in "1234567")
    }

    private fun displayMainMenu() {
        println("\nThis is the Main Menu")
        println("Choose a command from below:")
        println("\t1. Show all movies (edit)")
        println("\t2. Add new movie")
        println("\t3. Show all sessions (edit)")
        println("\t4. Add new session")
        println("\t5. Sell ticket")
        println("\t6. Return ticket")
        println("\t7. Scan ticket")
        println("\tEnter any other input to go back to registration menu.")
        printInputMessage("Only enter an index of chosen operation without any additional symbols:")
    }

    private fun printAndEditMovies() {
        val movies = movieManager.listMovies()
        if (movies.isEmpty()) {
            printErrorMessage("No movies available.")
            return
        }
        movies.forEachIndexed { index, movie -> println("$index: $movie") }
        printInputMessage("Enter the index of the movie to edit, or anything else to return:")
        val input = readln()
        val index = input.toIntOrNull()
        if (index == null || index !in movies.indices) {
            return
        }

        // option to delete this movie
        printInputMessage("Enter 'DELETE' to delete this movie or press enter to start editing")
        val delete = readln()
        if (delete == "DELETE") {
            sessionManager.deleteSessionsByMovie(movies[index])
            movieManager.deleteMovie(index)
            return
        }

        printInputMessage("Enter new name for the movie or press enter to skip:")
        val name = readln()
        //  printInputMessage("Enter new duration in minutes or press enter to skip:")
        // val duration = readln().toIntOrNull() ?: movies[index].duration
        printInputMessage("Enter new description or press enter to skip:")
        val description = readln()

        // val updatedMovie = Movie(name.ifBlank { movies[index].name }, duration, description.ifBlank { movies[index].description })
        val updatedMovie = Movie(name.ifBlank { movies[index].name },
            movies[index].duration,
            description.ifBlank { movies[index].description })
        movieManager.editMovie(index, updatedMovie)
        println("Movie updated successfully.")
    }

    private fun addMovie() {
        println("Enter movie name:")
        val name = readln()
        println("Enter movie duration in minutes:")
        val duration = readln().toIntOrNull() ?: return printErrorMessage("Invalid duration")
        println("Enter the description of this movie in one line:")
        val description = readln()

        val movie = Movie(name, duration, description)
        movieManager.addMovie(movie)
        println("Movie added successfully.")
    }

    private fun printAndEditSessions() {
        val sessions = sessionManager.listSessions()
        if (sessions.isEmpty()) {
            printErrorMessage("No sessions available.")
            return
        }
        sessions.forEachIndexed { index, session -> println("$index: $session") }

        printInputMessage("Enter the index of the session to edit, or anything else to return:")
        val input = readln()
        val index = input.toIntOrNull()
        if (index == null || index !in sessions.indices) {
            return
        }

        // option to delete this movie
        printInputMessage("Enter 'DELETE' to delete this movie or press enter to start editing")
        val delete = readln()
        if (delete == "DELETE") {
            sessionManager.deleteSession(index)
            return
        }

        addSession()
    }

    private fun addSession() {
        val movie = chooseMovie()

        var sessionStartTime: LocalDateTime? = null
        val formatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy")

        while (sessionStartTime == null || !sessionManager.canAddThisSessionTime(
                sessionStartTime,
                sessionStartTime.plusMinutes(movie.duration.toLong())
            )
        ) {
            printInputMessage("Enter session time in this format ('HH:mm dd.MM.yyyy'):")
            val sessionTime = readln()
            try {
                sessionStartTime = LocalDateTime.parse(sessionTime, formatter)
            } catch (e: DateTimeParseException) {
                printErrorMessage("Invalid date format. Please try again.")
            }
        }

        val sessionEndTime = sessionStartTime.plusMinutes(movie.duration.toLong())

        sessionManager.addSession(Session(movie, sessionStartTime, sessionEndTime))
        println("Session added successfully.")
    }

    private fun chooseMovie(): Movie {
        val movies = movieManager.listMovies()
        printInputMessage("Choose a movie by entering a number without any additional symbols:")
        movies.forEachIndexed { index, movie -> println("$index: $movie") }
        var index = readln().toIntOrNull()

        while (index == null || index !in movies.indices) {
            printErrorMessage("Error! Incorrect index.")
            printInputMessage("Enter an index of the movie you chose:")
            index = readln().toIntOrNull()
        }
        return movies[index]
    }

    private fun sellTicket() {
        val session = selectSession() ?: return

        // if session ended return
        if (LocalDateTime.now() > session.endTime) {
            printErrorMessage("This session has already ended, you cannot buy this ticket.")
            return
        }

        val seat = selectSeat(session)

        val result = session.bookSeat(seat.first, seat.second)
        println(result)
    }

    private fun returnTicket() {
        val session = selectSession() ?: return

        // if session started or ended return
        if (LocalDateTime.now() > session.endTime) {
            printErrorMessage("This session has already ended, you cannot return this ticket.")
            return
        } else if (LocalDateTime.now() > session.startTime) {
            printErrorMessage("This session has already started, you cannot return this ticket")
            return
        }

        val seat = selectSeat(session)

        val result = session.freeSeat(seat.first, seat.second)
        println(result)
    }

    private fun scanTicket() {
        val session = selectSession() ?: return

        // if more than 1 hour before session or session already ended return
        if (LocalDateTime.now() < session.startTime.minusHours(1)) {
            printErrorMessage("The ticket can be scanned no earlier than 1 hour before the session.")
            return
        } else if (LocalDateTime.now() > session.endTime) {
            printErrorMessage("This session has already finished.")
            return
        }

        val seat = selectSeat(session)

        val result = session.scanSeat(seat.first, seat.second)
        println(result)
    }

    private fun selectSession(): Session? {
        val sessions = sessionManager.listSessions()
        if (sessions.isEmpty()) {
            printErrorMessage("No sessions available.")
            return null
        }

        sessions.forEachIndexed { index, session -> println("$index: $session") }
        printInputMessage("Choose a session by entering its index:")
        val index = readln().toIntOrNull()
        if (index == null || index !in sessions.indices) {
            printErrorMessage("Invalid session index.")
            return null
        }
        return sessions[index]
    }

    private fun selectSeat(session: Session): Pair<Int, Int> {
        session.displaySeats()

        var row: Int? = null
        while (row == null || !session.rowExists(row)) {
            printInputMessage("Enter seat row:")
            row = readln().toIntOrNull()
        }

        var column: Int? = null
        while (column == null || !session.columnExists(column)) {
            printInputMessage("Enter seat column:")
            column = readln().toIntOrNull()
        }

        return Pair(row, column)
    }


    private fun printInputMessage(message: String) {
        println("\u001B[33m$message\u001B[0m")
    }

    private fun printErrorMessage(message: String = "Error! Incorrect input.") {
        println("\u001B[31m$message\u001B[0m")
    }
}
