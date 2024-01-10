package movie_theater_management.data_storage

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializer
import com.google.gson.reflect.TypeToken
import movie_theater_management.Movie
import movie_theater_management.Session
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class JSONDataStorage : IDataStorage {
    // path to JSON files
    private val moviesFilePath = "data/movies.json"
    private val sessionsFilePath = "data/sessions.json"

    // custom serializer
    private val localDateTimeSerializer = JsonSerializer<LocalDateTime> { src, _, _ ->
        JsonPrimitive(src.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
    }

    // custom deserializer
    private val localDateTimeDeserializer = JsonDeserializer { json, _, _ ->
        LocalDateTime.parse(json.asJsonPrimitive.asString, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    }

    // configure gson
    private val gson: Gson = GsonBuilder()
        .registerTypeAdapter(LocalDateTime::class.java, localDateTimeSerializer)
        .registerTypeAdapter(LocalDateTime::class.java, localDateTimeDeserializer)
        .create()

    override fun loadMovies(): List<Movie> {
        val file = File(moviesFilePath)
        if (file.exists()) {
            val type = object : TypeToken<List<Movie>>() {}.type
            return gson.fromJson(file.readText(), type)
        }
        return emptyList()
    }

    override fun saveMovies(movies: List<Movie>) {
        val json = gson.toJson(movies)
        File(moviesFilePath).writeText(json)
    }

    override fun loadSessions(): List<Session> {
        val file = File(sessionsFilePath)
        if (file.exists()) {
            val type = object : TypeToken<List<Session>>() {}.type
            return gson.fromJson(file.readText(), type)
        }
        return emptyList()
    }

    override fun saveSessions(sessions: List<Session>) {
        val json = gson.toJson(sessions)
        File(sessionsFilePath).writeText(json)
    }
}
