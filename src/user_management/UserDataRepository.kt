package user_management

interface UserDataRepository {
    fun loadUsers(): MutableMap<String, User>
    fun saveUsers(users: Map<String, User>)
}


