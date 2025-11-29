package com.example.levelup.RepositoryTest

import com.example.levelup.data.dto.AddItemRequest
import com.example.levelup.data.dto.CartResponse
import com.example.levelup.data.dto.ReplaceCartRequest
import com.example.levelup.data.network.CartApi
import com.example.levelup.data.repository.CartRepository
import com.example.levelup.model.CartItem
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk

class CartRepositoryTest : DescribeSpec({

    describe("CartRepository API calls") {

        it("getCart retorna respuesta del API") {
            val api = mockk<CartApi>()
            val repo = CartRepository(api)
            val resp = CartResponse(1L, emptyList(), 0, 0.0)
            coEvery { api.getCart(1L) } returns resp

            val r = repo.getCart(1L)
            r.isSuccess shouldBe true
            r.getOrNull() shouldBe resp
        }

        it("addToCart envía AddItemRequest y retorna respuesta") {
            val api = mockk<CartApi>()
            val repo = CartRepository(api)
            val req = AddItemRequest(10L, 2, "Mouse", 300, "/img")
            val resp = CartResponse(1L, listOf(CartItem(1L, 10L, "Mouse", 300, 2, "/img")), 2, 600.0)
            coEvery { api.addItem(1L, req) } returns resp

            val r = repo.addToCart(1L, req)
            r.getOrNull()!!.items.shouldHaveSize(1)
        }

        it("updateItemQuantity envía PATCH y retorna respuesta") {
            val api = mockk<CartApi>()
            val repo = CartRepository(api)
            val resp = CartResponse(1L, emptyList(), 0, 0.0)
            coEvery { api.updateQuantity(1L, 2L, any()) } returns resp

            val r = repo.updateItemQuantity(1L, 2L, 3)
            r.isSuccess shouldBe true
        }

        it("deleteItem llama DELETE y retorna respuesta") {
            val api = mockk<CartApi>()
            val repo = CartRepository(api)
            val resp = CartResponse(1L, emptyList(), 0, 0.0)
            coEvery { api.removeItem(1L, 2L) } returns resp

            val r = repo.deleteItem(1L, 2L)
            r.isSuccess shouldBe true
        }

        it("clearAllItems llama DELETE base y retorna vacío") {
            val api = mockk<CartApi>()
            val repo = CartRepository(api)
            val resp = CartResponse(1L, emptyList(), 0, 0.0)
            coEvery { api.clearCart(1L) } returns resp

            val r = repo.clearAllItems(1L)
            r.getOrNull()!!.items.shouldHaveSize(0)
        }

        it("replaceCart llama PUT con lista y retorna respuesta") {
            val api = mockk<CartApi>()
            val repo = CartRepository(api)
            val items = listOf(AddItemRequest(10L, 1, "Mouse", 300, "/img"))
            val resp = CartResponse(1L, emptyList(), 0, 0.0)
            coEvery { api.replaceCart(1L, ReplaceCartRequest(items)) } returns resp

            val r = repo.replaceCart(1L, items)
            r.isSuccess shouldBe true
        }
    }
})

