package com.example.levelup.data.dto

data class ReplaceCartRequest(
    val items: List<AddItemRequest> // O List<CartItem>, dependiendo de tu servidor
)