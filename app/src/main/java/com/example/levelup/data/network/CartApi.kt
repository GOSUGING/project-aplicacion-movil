package com.example.levelup.data.network

import com.example.levelup.model.CartItem
import retrofit2.http.*

interface CartApi {

    @GET("api/cart/{userId}")
    suspend fun getCart(@Path("userId") userId: Long): List<CartItem>

    @POST("api/cart/add")
    suspend fun addToCart(@Body item: Map<String, Any>): CartItem

    @DELETE("api/cart/{itemId}")
    suspend fun deleteItem(@Path("itemId") itemId: Long)
}
