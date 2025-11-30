package com.example.levelup.data.repository

import com.example.levelup.data.network.CartApi
import com.example.levelup.data.dto.CartResponse
import com.example.levelup.data.dto.AddItemRequest
import com.example.levelup.data.dto.UpdateQuantityRequest
import com.example.levelup.data.dto.ReplaceCartRequest
import javax.inject.Inject

/**
 * Repositorio para manejar las operaciones del carrito.
 * Conecta el ViewModel con la API de red (Retrofit).
 */
class CartRepository @Inject constructor(
    private val cartApi: CartApi
) {

    // -----------------------------------------------------------------
    // 1. OBTENER / CREAR CARRITO (GET)
    // Usado por CartViewModel.loadCart()
    // -----------------------------------------------------------------
    suspend fun getCart(userId: Long): Result<CartResponse> {
        // Usa runCatching para manejar excepciones de red/HTTP de forma segura
        return runCatching {
            cartApi.getCart(userId)
        }
    }

    // -----------------------------------------------------------------
    // 2. AGREGAR ÍTEM (POST)
    // Usado por CartViewModel.addProduct()
    // -----------------------------------------------------------------
    suspend fun addToCart(userId: Long, req: AddItemRequest): Result<CartResponse> {
        return runCatching {
            cartApi.addItem(userId, req)
        }
    }

    // -----------------------------------------------------------------
    // 3. ACTUALIZAR CANTIDAD (PATCH)
    // -----------------------------------------------------------------
    suspend fun updateItemQuantity(userId: Long, itemId: Long, newQuantity: Int): Result<CartResponse> {
        return runCatching {
            val request = UpdateQuantityRequest(newQuantity)
            cartApi.updateQuantity(userId, itemId, request)
        }
    }

    // -----------------------------------------------------------------
    // 4. ELIMINAR ÍTEM (DELETE)
    // Usado por CartViewModel.deleteItem()
    // -----------------------------------------------------------------
    suspend fun deleteItem(userId: Long, itemId: Long): Result<CartResponse> {
        return runCatching {
            cartApi.removeItem(userId, itemId)
        }
    }

    // -----------------------------------------------------------------
    // 5. LIMPIAR CARRITO COMPLETO (DELETE) / Borrar todos los ítems
    // Usado por CartViewModel.clearCart() después de un pago exitoso.
    // -----------------------------------------------------------------
    suspend fun clearAllItems(userId: Long): Result<CartResponse> {
        return runCatching {
            cartApi.clearCart(userId)
        }
    }

    // -----------------------------------------------------------------
    // 6. REEMPLAZAR CARRITO (PUT)
    // -----------------------------------------------------------------
    suspend fun replaceCart(userId: Long, items: List<AddItemRequest>): Result<CartResponse> {
        return runCatching {
            val request = ReplaceCartRequest(items)
            cartApi.replaceCart(userId, request)
        }
    }
}