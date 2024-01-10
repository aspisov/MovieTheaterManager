package user_management

import java.io.*

class FileUserDataRepository(private val storageFile: File) : UserDataRepository {
    override fun loadUsers(): MutableMap<String, User> {
        if (!storageFile.exists()) return mutableMapOf() // no data yet
        ObjectInputStream(FileInputStream(storageFile)).use { return it.readObject() as MutableMap<String, User> }
    }

    override fun saveUsers(users: Map<String, User>) {
        ObjectOutputStream(FileOutputStream(storageFile)).use { it.writeObject(users) }
    }
}