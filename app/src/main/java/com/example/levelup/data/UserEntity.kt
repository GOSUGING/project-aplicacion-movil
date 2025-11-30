package com.example.levelup.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

// Esta anotación define la tabla "users" en la base de datos.
// El índice asegura que no puedan existir dos usuarios con el mismo email.
@Entity(
    tableName = "users",
    indices = [Index(value = ["email"], unique = true)]
)
data class UserEntity(
    // La clave primaria 'id' se autogenera, y es de tipo Long para mayor capacidad.
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    // Las columnas de la tabla.
    val nombre: String,
    val email: String,
    val fechaNacimiento: String, // Formato sugerido: yyyy-MM-dd
    val passwordHash: String     // Importante: Aquí se debe guardar un HASH de la contraseña, no la contraseña en texto plano.
)
