package com.example.levelup.data.dto



data class ProductDTO(
    val id: Long,
    val name: String,
    val description: String?,
    val price: Int,
    val img: String,
    val category: String,
    val stock: Int
)
