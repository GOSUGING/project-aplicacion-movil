package com.example.levelup.data.network

import com.example.levelup.data.dto.CouponDTO
import retrofit2.http.GET
import retrofit2.http.Path

interface CouponsApi {

    @GET("api/coupons/{code}")
    suspend fun getCoupon(@Path("code") code: String): CouponDTO
}
