package com.example.levelup.viewmodel

import androidx.lifecycle.ViewModel
import com.example.levelup.data.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TopBarViewModel @Inject constructor(
    // Pide el SessionManager a Hilt, que sabe cómo crearlo como un Singleton
    sessionManager: SessionManager
) : ViewModel() {
    // Expone el estado del usuario actual a la UI de la TopBar
    val currentUser = sessionManager.currentUser
}
