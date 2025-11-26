package com.example.levelup.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelup.data.dto.LoginResponse
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
                "email" -> it.copy(email = value, error = null) // Limpiamos el error al escribir
                "password" -> it.copy(password = value, error = null)
                else -> it
            }
        }
    }

    fun getUserRole(): String {
        return sessionManager.getCurrentUser()?.role ?: ""
    }

    /**
     * Intenta iniciar sesión. Si tiene éxito, guarda la sesión y llama
     * a la callback 'onNavigate' con la ruta determinada por el rol.
     *
     * @param onNavigate Callback que recibe la ruta (String) a la que se debe navegar ("admin" o "profile").
     */
    fun login(onNavigate: (route: String) -> Unit) {
        val state = _ui.value

        // 1. Validación local
        if (state.email.isBlank() || state.password.isBlank()) {
            _ui.update { it.copy(error = "Completa todos los campos") }
            return
        }

        viewModelScope.launch {
            _ui.update { it.copy(isLoading = true, error = null) }

            // 2. Intento de autenticación (se asume que repo.login devuelve Result<LoginResponse>)
            val result = repo.login(state.email, state.password)

            result.fold(
                // 3. Éxito: Guardar sesión y determinar ruta
                onSuccess = { res ->

                    val session = UserSession(
                        id = res.id ?: -1,
                        name = res.name ?: "Usuario",
                        email = res.email ?: state.email,
                        role = res.role ?: "USER" // Valor por defecto seguro
                    )

                    sessionManager.setCurrentUser(session)

                    _ui.update { it.copy(isLoading = false, password = "") } // Limpiamos password

                    // Determinamos la ruta de navegación:
                    val route = if (session.role.equals("ADMIN", ignoreCase = true)) {
                        "admin" // Redirige al panel de administración
                    } else {
                        "profile" // Redirige al perfil estándar
                    }

                    // Llamamos a la callback con la ruta correcta
                    onNavigate(route)
                },

                // 4. Fallo: Mostrar error
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