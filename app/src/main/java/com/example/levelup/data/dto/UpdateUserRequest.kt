package com.example.levelup.data.dto

data class UpdateUserRequest(
    val name: String,
    val email: String,
    val address: String?,
    val phone: String?,
    val role: String? = null   // necesaria para promover a ADMIN
)
