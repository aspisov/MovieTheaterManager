package movie_theater_management.data_storage

import movie_theater_management.Movie
import movie_theater_management.Session

interface IDataStorage {
    fun loadMovies(): List<Movie>
    fun saveMovies(movies: List<Movie>)
    fun loadSessions(): List<Session>
    fun saveSessions(sessions: List<Session>)
}