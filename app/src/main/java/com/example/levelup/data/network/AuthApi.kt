package com.example.levelup.data.network

import com.example.levelup.data.dto.LoginRequest
import com.example.levelup.data.dto.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("api/auth/login")
    suspend fun login(@Body req: LoginRequest): LoginResponse
}
