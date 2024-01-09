package movie_theater_management

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class Session(val movie: Movie, var startTime: LocalDateTime, var endTime: LocalDateTime) {
    private val rows = 5
    private val columns = 10
    private val seats = Array(rows) { CharArray(columns) { '-' } }

    fun bookSeat(row: Int, column: Int): String {
        if (seats[row - 1][column - 1] == '-') {
            seats[row - 1][column - 1] = 'B'
            return "Seat $row $column is successfully booked"
        }
        return "Error! Seat $row $column is already booked"
    }

    fun freeSeat(row: Int, column: Int): String {
        if (seats[row - 1][column - 1] == 'B') {
            seats[row - 1][column - 1] = '-'
            return "Seat $row $column is no longer booked"
        }
        return "Seat $row $column was already free"
    }

    fun scanSeat(row: Int, column: Int): String {
        if (seats[row - 1][column - 1] == '-') {
            return "Error! Seat $row $column isn't booked"
        } else if (seats[row - 1][column - 1] == 'B') {
            seats[row - 1][column - 1] = 'P'
            return "Ticket successfully scanned"
        }
        return "Error! The owner of this seat has already scanned his ticket"
    }

    fun displaySeats() {
        println("'-' means seat is free")
        println("'B' means seat is booked")
        println("'P' means seat owner is already present")

        // here we print indexes of columns
        val indexesOfColumns = (1..<columns + 1).joinToString(separator = " ")
        println("  $indexesOfColumns")

        for (i in 0..<rows) {
            // here we print row index
            print("${i + 1} ")

            for (j in 0..<columns) {
                print("${seats[i][j]} ")
            }
            println()
        }
    }

    fun rowExists(row: Int): Boolean {
        return row in 1..<rows + 1
    }

    fun columnExists(column: Int): Boolean {
        return column in 1..<columns + 1
    }

    override fun toString(): String {
        val formatterTime = DateTimeFormatter.ofPattern("HH:mm")
        val formatterDate = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        var string = "$movie\n\tTime and date: ${startTime.format(formatterTime)} - ${endTime.format(formatterTime)}"
        string += "\t${startTime.format(formatterDate)}"
        return string
    }
}
