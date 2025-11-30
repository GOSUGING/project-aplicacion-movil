package com.example.levelup.data.dto

data class CartItemPayload(
    val productId: Long,
    val name: String,
    val qty: Int,
    val price: Int
)