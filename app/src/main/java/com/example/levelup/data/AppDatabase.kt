package com.example.levelup.data

import androidx.room.Database
import androidx.room.RoomDatabase

// La anotación @Database ahora funciona porque UserEntity::class es una referencia válida.
@Database(
    entities = [UserEntity::class], // La tabla que contendrá la base de datos
    version = 1,                   // La versión de la base de datos
    exportSchema = false           // No exportar el esquema para simplificar
)
abstract class AppDatabase : RoomDatabase() {
    // La función ahora funciona porque UserDao es una interfaz válida.
    // Room usará esta función para generar el código necesario para las operaciones.
    abstract fun userDao(): UserDao
}
