package com.example.levelup.ApiTest

import com.example.levelup.data.dto.CartResponse
import com.example.levelup.data.network.CartApi
import com.example.levelup.data.network.UserApi
import com.example.levelup.data.repository.CartRepository
import com.example.levelup.model.CartItem
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk

class DeleteViewModel : DescribeSpec({

    describe("DELETE de APIs con simulación usando Kotest + MockK") {

        it("CartRepository.deleteItem elimina un ítem y retorna éxito") {
            val api = mockk<CartApi>()
            val repo = CartRepository(api)

            val userId = 7L
            val itemId = 2L

            val items = listOf(
                CartItem(id = 1L, productId = 10L, name = "Mouse", price = 300, qty = 2, imageUrl = "url1")
            )

            val response = CartResponse(userId = userId, items = items, totalItems = 2, subtotal = 600.0)

            coEvery { api.removeItem(userId, itemId) } returns response

            val result = repo.deleteItem(userId, itemId)
            result.isSuccess shouldBe true
            result.getOrNull()?.items?.size shouldBe 1
            coVerify(exactly = 1) { api.removeItem(userId, itemId) }
        }

        it("CartRepository.deleteItem captura excepción y retorna failure") {
            val api = mockk<CartApi>()
            val repo = CartRepository(api)

            coEvery { api.removeItem(any(), any()) } throws RuntimeException("Error de red")

            val result = repo.deleteItem(1L, 99L)
            result.isFailure shouldBe true
            coVerify(exactly = 1) { api.removeItem(1L, 99L) }
        }

        it("CartRepository.clearAllItems vacía el carrito correctamente") {
            val api = mockk<CartApi>()
            val repo = CartRepository(api)

            val userId = 7L
            val response = CartResponse(userId = userId, items = emptyList(), totalItems = 0, subtotal = 0.0)
            coEvery { api.clearCart(userId) } returns response

            val result = repo.clearAllItems(userId)
            result.isSuccess shouldBe true
            result.getOrNull()?.items?.isEmpty() shouldBe true
            coVerify(exactly = 1) { api.clearCart(userId) }
        }

        it("CartRepository.clearAllItems retorna failure ante excepción") {
            val api = mockk<CartApi>()
            val repo = CartRepository(api)

            coEvery { api.clearCart(any()) } throws RuntimeException("Timeout")

            val result = repo.clearAllItems(5L)
            result.isFailure shouldBe true
            coVerify(exactly = 1) { api.clearCart(5L) }
        }

        it("UserApi.deleteUser invoca DELETE correctamente") {
            val api = mockk<UserApi>()
            coEvery { api.deleteUser(10L) } returns Unit

            api.deleteUser(10L)
            coVerify(exactly = 1) { api.deleteUser(10L) }
        }

        it("UserApi.deleteUser lanza excepción y se captura en test") {
            val api = mockk<UserApi>()
            coEvery { api.deleteUser(11L) } throws RuntimeException("No autorizado")

            var failed = false
            try {
                api.deleteUser(11L)
            } catch (e: RuntimeException) {
                failed = true
                e.message shouldBe "No autorizado"
            }

            failed shouldBe true
            coVerify(exactly = 1) { api.deleteUser(11L) }
        }
    }
})

