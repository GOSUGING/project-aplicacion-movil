package com.example.levelup.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelup.data.repository.UserRepository
import com.example.levelup.data.session.UserSession
import com.example.levelup.data.session.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val error: String? = null,
    val isLoading: Boolean = false
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repo: UserRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _ui = MutableStateFlow(LoginUiState())
    val ui = _ui.asStateFlow()

    fun onChange(field: String, value: String) {
        _ui.update {
            when (field) {
                "email" -> it.copy(email = value, error = null)
                "password" -> it.copy(password = value, error = null)
                else -> it
            }
        }
    }

    fun getUserRole(): String {
        return sessionManager.getCurrentUser()?.role ?: ""
    }

    // 🔥 IMPORTANTE: Aquí sacamos el ID real del usuario logueado
    fun currentUserId(): Long {
        return sessionManager.getCurrentUser()?.id ?: -1L
    }

    fun login(onNavigate: (route: String) -> Unit) {
        val state = _ui.value

        if (state.email.isBlank() || state.password.isBlank()) {
            _ui.update { it.copy(error = "Completa todos los campos") }
            return
        }

        viewModelScope.launch {
            _ui.update { it.copy(isLoading = true, error = null) }

            val result = repo.login(state.email, state.password)

            result.fold(
                onSuccess = { res ->

                    // Crea sesion inicial sin avatar (nullable)
                    val session = UserSession(
                        id = res.id ?: -1,
                        name = res.name ?: "Usuario",
                        email = res.email ?: state.email,
                        role = res.role ?: "USER",
                        avatar = null
                    )

                    sessionManager.setCurrentUser(session)

                    _ui.update { it.copy(isLoading = false, password = "") }

                    val route = if (session.role.equals("ADMIN", ignoreCase = true)) {
                        "admin"
                    } else {
                        "profile"
                    }

                    onNavigate(route)
                },

                onFailure = { error ->
                    _ui.update {
                        it.copy(
                            error = "Credenciales inválidas o error de conexión: ${error.message}",
                            isLoading = false
                        )
                    }
                }
            )
        }
    }
}
