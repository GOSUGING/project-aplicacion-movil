package com.example.levelup.data.dto

/**
 * DTO que representa la respuesta del servidor después de realizar un checkout exitoso.
 * @param message Contiene el mensaje de confirmación del servidor.
 */
data class CheckoutResponseDTO(
    val message: String
)