package com.example.levelup.data

import com.example.levelup.data.dto.ProductDTO
import com.example.levelup.data.network.ProductApi
import com.example.levelup.model.Product
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val api: ProductApi
) {

    suspend fun getProducts(): List<ProductDTO> {
        return api.getProducts()
    }

    suspend fun updateProduct(product: ProductDTO): ProductDTO {
        return api.updateProduct(product.id, product)
    }

    suspend fun getProductById(id: Long): ProductDTO = api.getProduct(id)
}
