package com.example.levelup.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelup.data.network.UserApi
import com.example.levelup.viewmodel.state.AdminUsersUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminUsersViewModel @Inject constructor(
    private val api: UserApi
) : ViewModel() {

    private val _ui = MutableStateFlow(AdminUsersUiState())
    val ui = _ui.asStateFlow()

    init {
        loadUsers()
    }

    fun loadUsers() {
        _ui.update { it.copy(loading = true, error = null) }

        viewModelScope.launch {
            try {
                val list = api.getUsers()
                _ui.update { it.copy(users = list, loading = false) }
            } catch (e: Exception) {
                _ui.update { it.copy(error = "Error cargando usuarios: ${e.message}", loading = false) }
            }
        }
    }

    fun toggleRole(id: Long) {
        viewModelScope.launch {
            try {
                val current = _ui.value.users.find { it.id == id }
                val newRole =
                    if (current?.role?.uppercase() == "ADMIN") "USER"
                    else "ADMIN"

                api.updateUserRole(
                    id = id,
                    req = mapOf("role" to newRole)
                )

                loadUsers()

            } catch (e: Exception) {
                _ui.update { it.copy(error = "Error cambiando rol: ${e.message}") }
            }
        }
    }

    fun deleteUser(id: Long) {
        viewModelScope.launch {
            try {
                api.deleteUser(id)
                loadUsers()
            } catch (e: Exception) {
                _ui.update { it.copy(error = "Error eliminando usuario: ${e.message}") }
            }
        }
    }
}
