package com.example.levelup.viewmodel.state

data class PaymentUiState(
    val loading: Boolean = false,
    val success: String,
    val error: String? = null
)
