package com.example.levelup.data.network

import com.example.levelup.data.dto.CheckoutRequest
import com.example.levelup.data.dto.CheckoutResponseDTO
import com.example.levelup.data.dto.PaymentDTO
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface PaymentApi {

    @POST("api/payments/checkout")
    suspend fun checkout(
        @Body body: CheckoutRequest   // ← usa TU DTO real
    ): CheckoutResponseDTO

    @GET("api/payments")
    suspend fun getAllPayments(): List<PaymentDTO>

    @GET("api/payments/{id}")
    suspend fun getPayment(@Path("id") id: Long): PaymentDTO
}
