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
    private val sessionManager: SessionManager   // ← CORREGIDO
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

    fun getUserRole(): String {
        return sessionManager.getCurrentUser()?.role ?: ""
    }

    fun login(onNavigate: (String) -> Unit) {
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

                    val session = UserSession(
                        id = res.id ?: -1,
                        name = res.name ?: "",
                        email = res.email ?: "",
                        role = res.role ?: ""
                    )

                    sessionManager.setCurrentUser(session)

                    _ui.update { it.copy(isLoading = false) }

                    if (session.role.equals("ADMIN", ignoreCase = true)) {
                        onNavigate("admin")
                    } else {
                        onNavigate("profile")
                    }
                },
                onFailure = {
                    _ui.update { it.copy(error = "Credenciales inválidas", isLoading = false) }
                }
            )
        }
    }
}
