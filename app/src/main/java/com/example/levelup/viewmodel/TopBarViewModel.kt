package com.example.levelup.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelup.data.session.SessionManager
import com.example.levelup.data.session.UserSession
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow // <-- Importación necesaria
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TopBarViewModel @Inject constructor(
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _currentUser = MutableStateFlow<UserSession?>(null)
    // CORRECCIÓN: Exponemos el MutableStateFlow como StateFlow inmutable
    val currentUser: StateFlow<UserSession?> = _currentUser.asStateFlow()

    init {
        // Cargar usuario al iniciar el ViewModel
        viewModelScope.launch {
            _currentUser.value = sessionManager.getCurrentUser()
        }
    }

    fun refreshUser() {
        // Utilizamos el mutable flow para actualizar el valor
        _currentUser.value = sessionManager.getCurrentUser()
    }
}