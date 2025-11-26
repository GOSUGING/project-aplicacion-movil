package com.example.levelup.data

import com.example.levelup.data.dto.PaymentDTO
import com.example.levelup.data.network.PaymentApi
import javax.inject.Inject

class PaymentRepository @Inject constructor(
    private val api: PaymentApi
) {

    suspend fun getPayments(): Result<List<PaymentDTO>> {
        return try {
            Result.success(api.getPayments())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun checkout(): Result<Unit> {
        return try {
            api.checkout()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
