package com.example.levelup.model

import com.example.levelup.data.dto.ProductDTO

data class ProductUiState(
    val products: List<ProductDTO> = emptyList(),
    val error: String? = null,
    val isLoading: Boolean = false,
    val successMessage: String? = null
)
