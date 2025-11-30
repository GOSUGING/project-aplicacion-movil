// Archivo: CardPaymentDTO.kt (ejemplo)
package com.example.levelup.data.dto

data class CardPaymentDTO(
    val cardNumber: String,
    val cardHolder: String,
    val expirationMonth: Int,
    val expirationYear: Int,
    val cvv: String
)