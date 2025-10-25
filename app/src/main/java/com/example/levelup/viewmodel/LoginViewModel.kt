package com.example.levelup.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelup.data.SessionManager
import com.example.levelup.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// Estado de la UI para la pantalla de Login
data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val error: String? = null,
    val isLoading: Boolean = false
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repo: UserRepository,
    private val sessionManager: SessionManager // Hilt inyecta ambos
) : ViewModel() {

    private val _ui = MutableStateFlow(LoginUiState())
    val ui = _ui.asStateFlow()

    fun onChange(field: String, value: String) {
        _ui.update {
            when (field) {
                "email" -> it.copy(email = value)
                "password" -> it.copy(password = value)
                else -> it
            }
        }
    }

    // La función de login ahora se encarga de todo el flujo
    fun login(onLoginSuccess: () -> Unit) {
        val s = _ui.value
        if (s.email.isBlank() || s.password.isBlank()) {
            _ui.update { it.copy(error = "Completa todos los campos") }
            return
        }

        viewModelScope.launch {
            _ui.update { it.copy(isLoading = true, error = null) }
            val result = repo.login(s.email, s.password)

            result.fold(
                onSuccess = { user ->
                    // ¡ÉXITO! Se establece el usuario en la sesión.
                    sessionManager.setCurrentUser(user)
                    _ui.update { it.copy(isLoading = false) }
                    onLoginSuccess() // Se llama al callback para navegar
                },
                onFailure = {
                    _ui.update { it.copy(error = "Credenciales inválidas", isLoading = false) }
                }
            )
        }
    }
}
