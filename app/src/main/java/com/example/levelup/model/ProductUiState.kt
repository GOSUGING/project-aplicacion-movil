package com.example.levelup.model

import com.example.levelup.data.dto.ProductDTO

data class ProductUiState(
    val products: List<ProductDTO> = emptyList(),

    // LISTA (ProductsScreen)
    val isLoading: Boolean = false,
    val error: String? = null,

    // DETALLE (ProductDetailScreen)
    val selectedProduct: ProductDTO? = null,
    val features: List<String> = emptyList()
)
