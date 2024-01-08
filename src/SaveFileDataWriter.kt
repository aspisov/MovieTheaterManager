import com.google.gson.*
import com.google.gson.reflect.TypeToken
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class SaveFileDataWriter {
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

    fun loadMovies(movies: MutableList<Movie>) {
        val file = File(moviesFilePath)
        if (file.exists()) {
            val type = object : TypeToken<List<Movie>>() {}.type
            movies.addAll(gson.fromJson(file.readText(), type))
        }
    }

    fun saveMovies(movies: MutableList<Movie>) {
        val json = gson.toJson(movies)
        File(moviesFilePath).writeText(json)
    }

    fun loadSessions(sessions: MutableList<Session>) {
        val file = File(sessionsFilePath)
        if (file.exists()) {
            val type = object : TypeToken<List<Session>>() {}.type
            sessions.addAll(gson.fromJson(file.readText(), type))
        }
    }

    fun saveSessions(sessions: MutableList<Session>) {
        sessions.sortBy { it.startTime }
        val json = gson.toJson(sessions)
        File(sessionsFilePath).writeText(json)
    }
}
