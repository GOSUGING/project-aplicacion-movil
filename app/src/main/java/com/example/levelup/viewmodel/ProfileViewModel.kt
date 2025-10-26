package com.example.levelup.viewmodel

import androidx.lifecycle.ViewModel
import com.example.levelup.data.SessionManager
import com.example.levelup.data.UserEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val sessionManager: SessionManager // Hilt inyecta el SessionManager
) : ViewModel() {

    // Expone el usuario actual a la UI
    val currentUser: StateFlow<UserEntity?> = sessionManager.currentUser

    fun logout() {
        sessionManager.logout()
    }
}
    