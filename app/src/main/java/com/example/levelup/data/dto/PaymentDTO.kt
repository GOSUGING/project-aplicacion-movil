package com.example.levelup.data.dto

data class PaymentDTO(
    val id: Long,
    val userId: Long,
    val amount: Int,
    val status: String,
    val date: String
)