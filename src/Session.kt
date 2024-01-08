import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class Session(val movie: Movie, var startTime: LocalDateTime, var endTime: LocalDateTime) {
    val rows = 5
    val columns = 10
    private val seats = Array(rows) { CharArray(columns) { '-' } }

    fun sellTicket(row: Int, column: Int): String {
        if (seats[row][column] == '-') {
            seats[row][column] = 'B'
            return "Seat $row $column is successfully booked"
        }
        return "Error! Seat $row $column is already booked"
    }

    fun returnTicket(row: Int, column: Int): String {
        if (seats[row][column] == 'B') {
            seats[row][column] = '-'
            return "Seat $row $column is no longer booked"
        }
        return "Seat $row $column was already free"
    }

    fun scanTicket(row: Int, column: Int): String {
        if (seats[row][column] == '-') {
            return "Error! Seat $row $column isn't booked"
        } else if (seats[row][column] == 'B') {
            seats[row][column] = 'P'
            return "Ticket successfully scanned"
        }
        return "Error! The owner of this seat has already scanned his ticket"
    }

    override fun toString(): String {
        val formatterTime = DateTimeFormatter.ofPattern("HH:mm")
        val formatterDate = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        return "Session(${movie.name}, time: ${startTime.format(formatterTime)}-${endTime.format(formatterTime)} " +
                "${startTime.format(formatterDate)})"
    }

    fun showAvailableSeats() {
        println("'-' means seat is free")
        println("'B' means seat is booked")
        println("'P' seat owner is already present")

        // here we print indexes of columns
        val indexesOfColumns = (0..<columns).joinToString(separator = " ")
        println("  $indexesOfColumns")

        for (i in 0..<rows) {
            // here we print row index
            print("$i ")

            for (j in 0..<columns) {
                print("${seats[i][j]} ")
            }
            println()
        }
    }
}