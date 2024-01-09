package movie_theater_management

class MovieManager(private val dataStorage: IDataStorage) {
    private val movies = mutableListOf<Movie>()

    init {
        movies.addAll(dataStorage.loadMovies())
    }

    fun addMovie(movie: Movie) {
        movies.add(movie)
        dataStorage.saveMovies(movies)
    }

    fun editMovie(index: Int, newMovie: Movie) {
        if (index in movies.indices) {
            movies[index] = newMovie
            dataStorage.saveMovies(movies)
        } else {
            throw IllegalArgumentException("Invalid movie index")
        }
    }

    fun deleteMovie(index: Int) {
        if (index in movies.indices) {
            movies.removeAt(index)
            dataStorage.saveMovies(movies)
        } else {
            throw IllegalArgumentException("Invalid movie index")
        }
    }

    fun listMovies(): List<Movie> {
        return movies
    }
}
