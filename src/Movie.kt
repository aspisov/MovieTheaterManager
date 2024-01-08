data class Movie(val name: String, val duration: Int, var description: String) {
    override fun toString(): String {
        return "Movie(Movie name: '$name', movie duration: $duration)\n\t$description"
    }

    fun changeDescription(newDescription: String) {
        description = newDescription
    }
}