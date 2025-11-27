package com.example.levelup.data.dto


data class CheckoutRequest(
    val userId: Long,
    val items: List<CheckoutItem>,
    val total: Int,
    val nombreUsuario: String,
    val direccionEnvio: String,
    val cardPayment: CardPaymentDTO
)
