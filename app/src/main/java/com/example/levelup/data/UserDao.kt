package com.example.levelup.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query // <--1. Asegúrate de que este import esté presente

@Dao
interface UserDao {

    /**
     * Inserta un usuario en la tabla.
     * @param user el usuario a ser insertado.
     * @return el ID de la fila del usuario recién insertado.
     */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(user: UserEntity): Long

    /**
     * Obtiene un usuario por su dirección de correo electrónico.
     * @param email el correo electrónico a buscar.
     * @return el UserEntity si se encuentra, o null si no existe.
     */
    // --- CORRECCIÓN AQUÍ ---
    // Se añade la anotación @Query para definir la consulta SQL que debe ejecutar esta función.
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1") // <-- 2. Esta línea fue añadida
    suspend fun getByEmail(email: String): UserEntity?
}
