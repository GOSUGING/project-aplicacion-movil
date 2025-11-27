package com.example.levelup.data.network

import com.example.levelup.data.dto.UserDTO
import retrofit2.http.*

interface UserApi {

    @GET("/api/users")
    suspend fun getUsers(): List<UserDTO>

    @DELETE("/api/users/{id}")
    suspend fun deleteUser(@Path("id") id: Long)

    @PUT("/api/users/{id}")
    suspend fun updateUserRole(
        @Path("id") id: Long,
        @Body req: Map<String, String>
    ): UserDTO
}
