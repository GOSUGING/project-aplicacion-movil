package com.example.levelup.data.network

import com.example.levelup.data.dto.LoginRequest
import com.example.levelup.data.dto.LoginResponse
import com.example.levelup.data.dto.RegisterRequest
import com.example.levelup.data.dto.RegisterResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("api/auth/login")
    suspend fun login(@Body req: LoginRequest): LoginResponse

    @POST("/api/auth/register")
    suspend fun register(@Body req: RegisterRequest): RegisterResponse
}
