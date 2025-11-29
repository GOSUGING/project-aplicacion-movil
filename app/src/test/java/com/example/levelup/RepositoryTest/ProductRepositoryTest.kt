package com.example.levelup.RepositoryTest

import com.example.levelup.data.ProductRepository
import com.example.levelup.data.dto.ProductDTO
import com.example.levelup.data.network.ProductApi
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk

class ProductRepositoryTest : DescribeSpec({

    describe("ProductRepository") {

        it("getProducts retorna lista del api") {
            val api = mockk<ProductApi>()
            val repo = ProductRepository(api)

            val list = listOf(ProductDTO(1L, "Mouse", "D", 100, "/img", "peripherals", 10))
            coEvery { api.getProducts() } returns list

            repo.getProducts() shouldBe list
        }

        it("updateProduct retorna producto actualizado") {
            val api = mockk<ProductApi>()
            val repo = ProductRepository(api)

            val p = ProductDTO(1L, "Mouse", "D", 100, "/img", "peripherals", 10)
            val updated = p.copy(stock = 12)
            coEvery { api.updateProduct(1L, updated) } returns updated

            repo.updateProduct(updated) shouldBe updated
        }
    }
})

