package com.example.levelup.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelup.data.UserRepository
import com.example.levelup.data.session.UserSession
import com.example.levelup.data.session.SessionManager     // ← ESTE ES EL BUENO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


// Estado de la UI del perfil
data class ProfileUiState(
    val name: String = "",
    val email: String = "",
    val address: String = "",
    val phone: String = "",
    val newsletter: Boolean = false,
    val promos: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repo: UserRepository,
    private val sessionManager: SessionManager    // ← CORRECTO
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
                    // Campos no incluidos en tu backend actual
                    address = "",
                    phone = ""
                )
            }
        }
    }

    // Actualiza campos del formulario
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

    // Guarda el perfil — por ahora solo en sesión local
    fun saveProfile() {
        if (uiState.name.isBlank() || !uiState.email.contains("@")) {
            uiState = uiState.copy(errorMessage = "Nombre o email inválidos")
            return
        }

        viewModelScope.launch {

            val current = sessionManager.getCurrentUser()
            if (current != null) {
                val updatedUser = current.copy(
                    name = uiState.name,
                    email = uiState.email
                )

                sessionManager.setCurrentUser(updatedUser)

                uiState = uiState.copy(
                    successMessage = "Perfil actualizado correctamente",
                    errorMessage = null
                )
            }
        }
    }

    // Logout
    fun logout() {
        sessionManager.logout()

    }
}
