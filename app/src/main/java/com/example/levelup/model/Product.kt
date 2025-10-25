package com.example.levelup.model

data class Product(
    val id: Int,
    val name: String,
    val description: String,
    val price: Long,
    val imageRes: Int? = null,   // resource drawable id (opcional)
    val imageUrl: String? = null, // o URL
    val category: String = ""
)
