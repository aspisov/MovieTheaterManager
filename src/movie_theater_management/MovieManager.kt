package movie_theater_management

import movie_theater_management.data_storage.IDataStorage

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

    fun movieAlreadyExist(name: String): Boolean {
        return movies.find { it.name == name } != null
    }
}
