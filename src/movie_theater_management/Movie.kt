package movie_theater_management

data class Movie(val name: String, val duration: Int, var description: String) {
    override fun toString(): String {
        return "$name, movie duration: $duration\n\tDescription: $description"
    }
}