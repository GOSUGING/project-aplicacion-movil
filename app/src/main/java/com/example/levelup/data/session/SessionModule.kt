package com.example.levelup.di

import android.content.Context
import com.example.levelup.data.session.SessionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// SessionModule.kt (Línea 19 probablemente está dentro de este método)
@Module
@InstallIn(SingletonComponent::class)
object SessionModule {

    @Singleton
    @Provides
    // Hilt inyectará el Contexto de la Aplicación aquí automáticamente
    fun provideSessionManager(@ApplicationContext context: Context): SessionManager {
        // Asegúrate de que estás usando la implementación de SessionManager aquí
        return SessionManager(context)
    }
}
// Necesitas la importación:
// import dagger.hilt.android.qualifiers.ApplicationContext
// import android.content.Context