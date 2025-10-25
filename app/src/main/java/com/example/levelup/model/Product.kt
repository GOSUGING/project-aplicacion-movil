package com.example.levelup.model

import androidx.annotation.DrawableRes

data class Product(
    val id: Int,
    val name: String,
    val description: String,
    val price: Double,
    val imageUrl: String?, // Puede ser nulo si usamos un drawable local
    @DrawableRes val imageRes: Int?, // <-- AÑADE ESTO
    val category: String // <-- AÑADE ESTO
)
