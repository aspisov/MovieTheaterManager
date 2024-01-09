package movie_theater_management

import java.time.LocalDateTime

class SessionManager(private val dataStorage: IDataStorage) {
    private val sessions = mutableListOf<Session>()

    init {
        sessions.addAll(dataStorage.loadSessions())
    }

    fun addSession(session: Session) {
        sessions.add(session)
        sessions.sortedBy { it.startTime }
        dataStorage.saveSessions(sessions)
    }

//    fun editSession(index: Int, newSession: Session) {
//        if (index in sessions.indices) {
//            sessions[index] = newSession
//            dataStorage.saveSessions(sessions)
//        } else {
//            throw IllegalArgumentException("Invalid session index")
//        }
//    }

    fun deleteSession(index: Int) {
        if (index in sessions.indices) {
            sessions.removeAt(index)
            dataStorage.saveSessions(sessions)
        } else {
            throw IllegalArgumentException("Invalid session index")
        }
    }

    fun deleteSessionsByMovie(movie: Movie) {
        sessions.removeIf { it.movie == movie }
    }

    fun listSessions(): List<Session> {
        return sessions
    }

    fun canAddThisSessionTime(start: LocalDateTime, end: LocalDateTime): Boolean {
        for (session in sessions) {
            if (start > session.endTime || end < session.startTime) {
                continue
            }
            return false
        }
        return true
    }
}