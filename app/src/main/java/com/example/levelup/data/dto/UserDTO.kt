package com.example.levelup.data.dto

data class UserDTO(
    val id: Long,
    val name: String,
    val email: String,
    val address: String?,
    val phone: String?,
    val role: String?
)
