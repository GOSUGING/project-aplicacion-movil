package com.example.levelup.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelup.data.UserRepository
import com.example.levelup.data.session.SessionManager
import com.example.levelup.data.session.UserSession
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

// Estado completo del UI
data class ProfileUiState(
    val name: String = "",
    val email: String = "",
    val role: String = "",          // 👈 AGREGADO
    val address: String = "",
    val phone: String = "",
    val newsletter: Boolean = false,
    val promos: Boolean = false,
    val avatar: String? = null, // Uri de la imagen de perfil
    val successMessage: String? = null,
    val errorMessage: String? = null
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repo: UserRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    var uiState by mutableStateOf(ProfileUiState())
        private set

    init {
        loadCurrentUser()
    }

    private fun loadCurrentUser() {
        viewModelScope.launch {
            val user: UserSession? = sessionManager.getCurrentUser()

            if (user != null) {
                uiState = uiState.copy(
                    name = user.name,
                    email = user.email,
                    role = user.role ?: "USER",   // 👈 AGREGADO
                    avatar = user.avatar, // Carga avatar persistido en sesion
                    // Si tu backend no incluye estos campos, van vacíos
                    address = "",
                    phone = ""
                )
            }
        }
    }

    fun onFieldChange(field: String, value: Any) {
        uiState = when (field) {
            "name" -> uiState.copy(name = value as String)
            "email" -> uiState.copy(email = value as String)
            "address" -> uiState.copy(address = value as String)
            "phone" -> uiState.copy(phone = value as String)
            "newsletter" -> uiState.copy(newsletter = value as Boolean)
            "promos" -> uiState.copy(promos = value as Boolean)
            else -> uiState
        }
    }

    fun saveProfile() {
        if (uiState.name.isBlank() || !uiState.email.contains("@")) {
            uiState = uiState.copy(errorMessage = "Nombre o email inválidos")
            return
        }

        viewModelScope.launch {
            val current = sessionManager.getCurrentUser()
            if (current != null) {

                // Persistir cambios del perfil, conservando avatar
                val updatedUser = current.copy(
                    name = uiState.name,
                    email = uiState.email,
                    role = uiState.role,
                    avatar = uiState.avatar
                )

                sessionManager.setCurrentUser(updatedUser)

                uiState = uiState.copy(
                    successMessage = "Perfil actualizado correctamente",
                    errorMessage = null
                )
            }
        }
    }

    // Actualiza avatar y persiste en SessionManager
    fun updateAvatar(uri: String) {
        viewModelScope.launch {
            val current = sessionManager.getCurrentUser()
            if (current != null) {
                val updatedUser = current.copy(avatar = uri)
                sessionManager.setCurrentUser(updatedUser)
                uiState = uiState.copy(
                    avatar = uri,
                    successMessage = "Se ha cambiado su foto de perfil de manera exitosa",
                    errorMessage = null
                )
            }
        }
    }

    fun logout() {
        sessionManager.logout()
    }
}
