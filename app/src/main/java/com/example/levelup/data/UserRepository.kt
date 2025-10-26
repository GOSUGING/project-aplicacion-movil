package com.example.levelup.data

import javax.inject.Inject
import javax.inject.Singleton

// Con @Singleton, Hilt se asegura de que solo haya una instancia de este repositorio en toda la app.
// Con @Inject constructor, Hilt sabe cómo construir esta clase y le inyectará el UserDao.
@Singleton
class UserRepository @Inject constructor(private val dao: UserDao) {

    /**
     * Registra un nuevo usuario en la base de datos.
     * Devuelve un Result que contiene el ID del nuevo usuario si es exitoso, o una excepción si falla.
     */
    suspend fun register(user: UserEntity): Result<Long> = try {
        val newUserId = dao.insert(user)
        Result.success(newUserId)
    } catch (e: Exception) {
        // La causa más común de fallo aquí es una colisión de email (por el índice UNIQUE).
        Result.failure(e)
    }

    /**
     * Autentica a un usuario basado en su email y contraseña.
     * Devuelve un Result que contiene el UserEntity completo si es exitoso, o una excepción si falla.
     */
    suspend fun login(email: String, password: String): Result<UserEntity> {
        return try {
            // 1. Busca al usuario por su email.
            val user = dao.getByEmail(email.trim().lowercase())

            if (user == null) {
                // 2. Si no se encuentra el usuario, el login falla.
                Result.failure(Exception("Usuario no encontrado"))
            } else if (user.passwordHash != password) {
                // 3. Si la contraseña no coincide, el login falla.
                // NOTA DE SEGURIDAD: En una app real, aquí se deben comparar hashes.
                // Ejemplo: if (!BCrypt.check(password, user.passwordHash)) { ... }
                Result.failure(Exception("Contraseña incorrecta"))
            } else {
                // 4. Si el usuario y la contraseña son correctos, el login es exitoso.
                Result.success(user)
            }
        } catch (e: Exception) {
            // Captura cualquier otro error de base de datos.
            Result.failure(e)
        }
    }
}
