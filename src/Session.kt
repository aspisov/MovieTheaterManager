import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class Session(val movie: Movie, var startTime:LocalDateTime, var endTime:LocalDateTime) {
    private val rows = 5
    private val columns = 10
    private val seatIsBooked = Array(rows) { BooleanArray(columns) { false } }

    fun sellTicket(row: Int, column: Int) {
        if (!seatIsBooked[row][column]) {
            seatIsBooked[row][column] = true
            println("Seat $row $column is successfully purchased")
        } else {
            println("Error! Seat $row $column is already booked")
        }
    }

    fun returnTicket(row: Int, column: Int) {
        if (seatIsBooked[row][column]) {
            seatIsBooked[row][column] = false
            println("Seat $row $column is no longer booked")
        } else {
            println("Seat $row $column was already free")
        }
    }

    override fun toString(): String {
        val formatterTime = DateTimeFormatter.ofPattern("HH:mm")
        val formatterDate = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        return "Session(${movie.name}, time: ${startTime.format(formatterTime)}-${endTime.format(formatterTime)} " +
                "${startTime.format(formatterDate)})"
    }

    fun showAvailableSeats() {
        // here we print indexes of columns
        val indexesOfColumns = (0..<columns).joinToString(separator = " ")
        println("  $indexesOfColumns")

        for (i in 0..<rows) {
            // here we print row index
            print("$i ")

            for (j in 0..<columns) {
                if (seatIsBooked[i][j]) {
                    print("X ")
                } else {
                    print("- ")
                }
            }
            println()
        }
    }
}