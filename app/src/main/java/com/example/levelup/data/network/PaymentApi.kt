package com.example.levelup.data.network

import com.example.levelup.data.dto.PaymentDTO
import retrofit2.http.GET
import retrofit2.http.POST

interface PaymentApi {

    @GET("api/payments")
    suspend fun getPayments(): List<PaymentDTO>

    @POST("api/payments/checkout")
    suspend fun checkout()
}
