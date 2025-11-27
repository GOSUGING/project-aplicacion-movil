package com.example.levelup.data.dto

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
    val address: String,
    val phone: String,
    val fechaNacimiento: String,   // formato YYYY-MM-DD
    val role: String = "USER"      // por defecto
)
