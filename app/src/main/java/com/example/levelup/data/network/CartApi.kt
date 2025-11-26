package com.example.levelup.data.network

import com.example.levelup.data.dto.CartResponse
import com.example.levelup.data.dto.AddItemRequest
import com.example.levelup.data.dto.ReplaceCartRequest
import com.example.levelup.data.dto.UpdateQuantityRequest // Requerido para PATCH
import retrofit2.http.*

interface CartApi {

    // =================================================================
    // 1. OBTENER / CREAR CARRITO (GET)
    // Endpoint: GET /api/v1/cart/{userId}
    // =================================================================
    @GET("api/cart/{userId}")
    suspend fun getCart(
        @Path("userId") userId: Long
    ): CartResponse

    // =================================================================
    // 2. AGREGAR ÍTEM (POST)
    // Endpoint: POST /api/v1/cart/{userId}/items
    // =================================================================
    @POST("api/cart/{userId}/items")
    suspend fun addItem(
        @Path("userId") userId: Long,
        @Body req: AddItemRequest
    ): CartResponse


    // =================================================================
    // 3. ACTUALIZAR CANTIDAD (PATCH)
    // Endpoint: PATCH /api/v1/cart/{userId}/items/{itemId}
    // Asumimos que el body es UpdateQuantityRequest
    // =================================================================
    @PATCH("api/cart/{userId}/items/{itemId}")
    suspend fun updateQuantity(
        @Path("userId") userId: Long,
        @Path("itemId") itemId: Long,
        @Body req: UpdateQuantityRequest // DTO con la nueva cantidad
    ): CartResponse

    // =================================================================
    // 4. REMOVER ÍTEM (DELETE)
    // Endpoint: DELETE /api/v1/cart/{userId}/items/{itemId}
    // =================================================================
    @DELETE("api/cart/{userId}/items/{itemId}")
    suspend fun removeItem(
        @Path("userId") userId: Long,
        @Path("itemId") itemId: Long
    ): CartResponse

    // =================================================================
    // 5. LIMPIAR CARRITO COMPLETO (DELETE)
    // Endpoint: DELETE /api/v1/cart/{userId}
    // =================================================================
    @DELETE("api/cart/{userId}")
    suspend fun clearCart(
        @Path("userId") userId: Long
    ): CartResponse

    // =================================================================
    // 6. REEMPLAZAR CARRITO (PUT)
    // Endpoint: PUT /api/v1/cart/{userId}
    // =================================================================
    @PUT("api/cart/{userId}")
    suspend fun replaceCart(
        @Path("userId") userId: Long,
        @Body req: ReplaceCartRequest // Debe contener la nueva lista de ítems
    ): CartResponse
}