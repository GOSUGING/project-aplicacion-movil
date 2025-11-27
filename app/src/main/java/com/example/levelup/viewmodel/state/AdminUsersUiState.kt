package com.example.levelup.viewmodel.state

import com.example.levelup.data.dto.UserDTO

data class AdminUsersUiState(
    val users: List<UserDTO> = emptyList(),
    val loading: Boolean = false,
    val error: String? = null
)
