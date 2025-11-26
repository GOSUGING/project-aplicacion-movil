package com.example.levelup.data.session

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.core.content.edit


class SessionManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    // Estado en runtime
    private val _currentUser = MutableStateFlow<UserSession?>(null)
    val currentUser: StateFlow<UserSession?> = _currentUser.asStateFlow()

    init {
        loadSavedSession()
    }

    // -----------------------------------------------------
    // Carga sesión desde SharedPreferences al iniciar la app
    // -----------------------------------------------------
    private fun loadSavedSession() {
        val id = prefs.getLong("id", -1)
        val name = prefs.getString("name", null)
        val email = prefs.getString("email", null)
        val role = prefs.getString("role", null)

        if (id != -1L && name != null && email != null && role != null) {
            _currentUser.value = UserSession(id, name, email, role)
        }
    }

    // -----------------------------------------------------
    // Guarda sesión
    // -----------------------------------------------------
    fun setCurrentUser(user: UserSession) {
        prefs.edit {
            putLong("id", user.id)
                .putString("name", user.name)
                .putString("email", user.email)
                .putString("role", user.role)
        }

        _currentUser.value = user
    }

    // -----------------------------------------------------
    // Obtiene el usuario actual
    // -----------------------------------------------------
    fun getCurrentUser(): UserSession? = _currentUser.value

    // -----------------------------------------------------
    // 🔥 LOGOUT REAL (borra todo)
    // -----------------------------------------------------
    fun logout() {
        prefs.edit().clear().apply()  // borra almacenamiento persistente
        _currentUser.value = null     // borra estado en memoria
    }
}
