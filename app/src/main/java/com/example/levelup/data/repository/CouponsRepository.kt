package com.example.levelup.data.repository

import com.example.levelup.data.dto.CouponDTO
import com.example.levelup.data.network.CouponsApi
import javax.inject.Inject

class CouponsRepository @Inject constructor(
    private val api: CouponsApi
) {

    suspend fun findCoupon(code: String): Result<CouponDTO> {
        return try {
            val coupon = api.getCoupon(code)
            Result.success(coupon)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
