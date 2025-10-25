package com.example.levelup.data

import android.content.Context
import androidx.room.Room

object DbModule {
    @Volatile private var INSTANCE: AppDatabase? = null

    fun db(context: Context): AppDatabase =
        INSTANCE ?: synchronized(this) {
            INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "levelup.db"
            ).build().also { INSTANCE = it }
        }
}
