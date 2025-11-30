package com.example.levelup.data.session

data class UserSession(
    val id: Long,
    val name: String,
    val email: String,
    val role: String,
    val avatar: String? = null // Uri de la foto de perfil (nullable)
)
