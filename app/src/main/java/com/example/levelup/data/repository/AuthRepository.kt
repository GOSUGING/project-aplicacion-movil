package com.example.levelup.data.repository


import com.example.levelup.data.network.AuthApi
import com.example.levelup.data.dto.LoginRequest
import com.example.levelup.data.dto.LoginResponse
import javax.inject.Inject


class UserRepository @Inject constructor(
    private val api: AuthApi
) {
    suspend fun login(email: String, password: String): Result<LoginResponse> {
        return try {
            val req = LoginRequest(email, password)
            val res = api.login(req)   // <-- ESTO ES SUSPEND Y SÍ SE EJECUTA
            Result.success(res)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
