package com.example.levelup.data.dto

data class RegisterResponse(
    val id: Long?,
    val name: String,
    val email: String,
    val role: String,
    val token: String?,
    val message: String
)
