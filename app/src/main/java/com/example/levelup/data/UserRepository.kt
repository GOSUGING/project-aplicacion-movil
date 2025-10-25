package com.example.levelup.data

class UserRepository(private val dao: UserDao) {
    suspend fun register(user: UserEntity): Result<Long> = try {
        Result.success(dao.insert(user))
    } catch (e: Exception) {
        Result.failure(e) // colision email u otros errores SQL
    }

    suspend fun getByEmail(email: String) = dao.getByEmail(email)
}
