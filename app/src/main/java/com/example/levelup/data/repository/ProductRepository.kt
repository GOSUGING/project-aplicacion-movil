package com.example.levelup.data

import com.example.levelup.data.dto.ProductDTO
import com.example.levelup.data.network.ProductApi
import com.example.levelup.model.Product
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val api: ProductApi
) {

    // MAPPER DTO → UI
    private fun ProductDTO.toUI(): Product {
        return Product(
            id = id,
            name = name,
            description = description,
            price = price,
            imageUrl = img,
            imageRes = null,
            category = category,
            stock = stock
        )
    }

    // 🔥 OBTENER PRODUCTOS
    suspend fun getProducts(): Result<List<Product>> {
        return try {
            val dtoList = api.getProducts()
            val uiList = dtoList.map { it.toUI() }
            Result.success(uiList)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // 🔥🔥 ACTUALIZAR STOCK (SUMAR O RESTAR)
    suspend fun updateStock(id: Long, cantidad: Int): Result<Unit> {
        return try {
            api.updateStock(id = id, cantidad = cantidad)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
