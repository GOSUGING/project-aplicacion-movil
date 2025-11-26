package com.example.levelup.data.network

import com.example.levelup.data.dto.ProductDTO
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path


interface ProductApi {

    @GET("api/v1/products")
    suspend fun getProducts(): List<ProductDTO>

    @PUT("api/v1/products/descontar/{id}/{cantidad}")
    suspend fun updateStock(
        @Path("id") id: Long,
        @Path("cantidad") cantidad: Int
    ): String
}


