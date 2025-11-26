package com.example.levelup.data

import com.example.levelup.data.network.CartApi
import com.example.levelup.model.CartItem
import javax.inject.Inject


class CartRepository @Inject constructor(
    private val api: CartApi
) {

    suspend fun getCart(userId: Long): Result<List<CartItem>> {
        return try {
            Result.success(api.getCart(userId))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addToCart(userId: Long, productId: Long, qty: Int): Result<Unit> {
        return try {
            val body = mapOf(
                "userId" to userId,
                "productId" to productId,
                "quantity" to qty
            )
            api.addToCart(body)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteItem(itemId: Long): Result<Unit> {
        return try {
            api.deleteItem(itemId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
