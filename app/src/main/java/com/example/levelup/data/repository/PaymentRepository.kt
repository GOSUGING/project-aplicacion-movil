package com.example.levelup.data.repository

import com.example.levelup.data.dto.CardPaymentDTO
import com.example.levelup.data.dto.CheckoutItem
import com.example.levelup.data.dto.CheckoutRequest
import com.example.levelup.data.dto.CheckoutResponseDTO
import com.example.levelup.data.dto.PaymentDTO
import com.example.levelup.data.network.PaymentApi
import javax.inject.Inject

class PaymentRepository @Inject constructor(
    private val api: PaymentApi // Inyección de la API REAL
) {

    suspend fun checkout(
        userId: Long,
        items: List<CheckoutItem>,
        total: Int,
        nombreUsuario: String,
        direccionEnvio: String,
        cardPaymentDTO: CardPaymentDTO // DTO que contiene el número de tarjeta
    ): Result<CheckoutResponseDTO> {

        val cardNumber = cardPaymentDTO.cardNumber

        // ===============================================
        // 🛑 1. LÓGICA DE SIMULACIÓN LOCAL (Intercepta tarjetas de prueba)
        // ===============================================
        return when {
            // Éxito SIMULADO: Tarjeta termina en 1111
            cardNumber.endsWith("1111") -> {
                Result.success(CheckoutResponseDTO(
                    message = "¡Simulación de Pago EXITOSA! Carrito vaciado (Local)."
                ))
            }
            // Fallo SIMULADO: Tarjeta termina en 0000
            cardNumber.endsWith("0000") -> {
                Result.failure(RuntimeException("Simulación: Tarjeta rechazada por la pasarela de prueba."))
            }

            // ===============================================
            // 🌐 2. LÓGICA DE PRODUCCIÓN (Llama a la red real)
            // ===============================================
            else -> runCatching {
                // Se construye el cuerpo SOLO si no es una tarjeta simulada.
                val body = CheckoutRequest(
                    userId = userId, // Asumiendo que CheckoutRequest usa 'id'
                    items = items,
                    total = total,
                    nombreUsuario = nombreUsuario,
                    direccionEnvio = direccionEnvio,
                    cardPayment = cardPaymentDTO
                )

                // 🛑 Llama a la API REAL si la tarjeta no era de prueba
                api.checkout(body)
            }
        }
    }

    suspend fun getPayments(): Result<List<PaymentDTO>> = runCatching {
        api.getAllPayments()
    }

    suspend fun getPayment(id: Long): Result<PaymentDTO> = runCatching {
        api.getPayment(id)
    }
}