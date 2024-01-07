fun main() {
//    val currentDateTime = LocalDateTime.now()
//    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
//    val formattedDateTime = currentDateTime.format(formatter)  // Format to String
//
//    println("Formatted Date and Time: $formattedDateTime")
//
//    // Parsing back
//    val parsedDateTime = LocalDateTime.parse(formattedDateTime, formatter)
//    println("Parsed Date and Time: $parsedDateTime")
    val cinema = CinemaManager()
    cinema.startApplication()
}