package com.example.levelup.ApiTest

import com.example.levelup.data.dto.CouponDTO
import com.example.levelup.data.dto.PaymentDTO
import com.example.levelup.data.dto.ProductDTO
import com.example.levelup.data.dto.UserDTO
import com.example.levelup.data.dto.CartResponse
import com.example.levelup.model.CartItem
import com.example.levelup.data.network.CartApi
import com.example.levelup.data.network.CouponsApi
import com.example.levelup.data.network.PaymentApi
import com.example.levelup.data.network.ProductApi
import com.example.levelup.data.network.UserApi
import com.example.levelup.data.repository.PaymentRepository
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.collections.shouldHaveSize
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk

class GetViewModel : DescribeSpec({

    describe("GET de APIs con simulación usando Kotest + MockK") {

        it("PaymentRepository.getPayments retorna lista de pagos") {
            val api = mockk<PaymentApi>()
            val repo = PaymentRepository(api)

            val payments = listOf(
                PaymentDTO(
                    id = 1L,
                    cantidad = 3,
                    total = 1500L,
                    estado = "PAID",
                    fecha = "2025-11-29",
                    nombreUsuario = "Usuario",
                    direccionEnvio = "Calle 123",
                    userId = 10L,
                    rawPayload = "{}"
                ),
                PaymentDTO(
                    id = 2L,
                    cantidad = 1,
                    total = 2500L,
                    estado = "PAID",
                    fecha = "2025-11-28",
                    nombreUsuario = "Usuario",
                    direccionEnvio = "Calle 123",
                    userId = 10L,
                    rawPayload = "{}"
                )
            )

            coEvery { api.getAllPayments() } returns payments

            val result = repo.getPayments()

            result.isSuccess shouldBe true
            result.getOrNull()!!.shouldHaveSize(2)
            coVerify(exactly = 1) { api.getAllPayments() }
        }

        it("PaymentRepository.getPayments captura excepción y retorna failure") {
            val api = mockk<PaymentApi>()
            val repo = PaymentRepository(api)

            coEvery { api.getAllPayments() } throws RuntimeException("Network down")

            val result = repo.getPayments()
            result.isFailure shouldBe true
            coVerify(exactly = 1) { api.getAllPayments() }
        }

        it("PaymentRepository.getPayment obtiene un pago por id") {
            val api = mockk<PaymentApi>()
            val repo = PaymentRepository(api)

            val payment = PaymentDTO(
                id = 99L,
                cantidad = 2,
                total = 999L,
                estado = "PAID",
                fecha = "2025-11-20",
                nombreUsuario = "Tester",
                direccionEnvio = "Av 1",
                userId = 20L,
                rawPayload = "{}"
            )
            coEvery { api.getPayment(99L) } returns payment

            val result = repo.getPayment(99L)
            result.isSuccess shouldBe true
            result.getOrNull()!!.id shouldBe 99L
            coVerify(exactly = 1) { api.getPayment(99L) }
        }

        it("ProductApi.getProducts retorna lista de productos simulada") {
            val api = mockk<ProductApi>()

            val products = listOf(
                ProductDTO(1L, "Mouse Pro", "Gaming mouse", 300, "url1", "peripherals", 15),
                ProductDTO(2L, "Teclado", "Mecánico", 500, "url2", "peripherals", 5)
            )

            coEvery { api.getProducts() } returns products

            val result = api.getProducts()
            result.shouldHaveSize(2)
            result[0].name shouldBe "Mouse Pro"
            coVerify(exactly = 1) { api.getProducts() }
        }

        it("UserApi.getUsers retorna lista de usuarios simulada") {
            val api = mockk<UserApi>()
            val users = listOf(
                UserDTO(1L, "Ana", "ana@example.com", "Calle 1", "555111", "USER"),
                UserDTO(2L, "Bruno", "bruno@example.com", null, null, "ADMIN")
            )

            coEvery { api.getUsers() } returns users

            val result = api.getUsers()
            result.shouldHaveSize(2)
            result[1].role shouldBe "ADMIN"
            coVerify(exactly = 1) { api.getUsers() }
        }

        it("CartApi.getCart retorna estado del carrito simulado") {
            val api = mockk<CartApi>()
            val items = listOf(
                CartItem(id = 1L, productId = 10L, name = "Mouse", price = 300, qty = 2, imageUrl = "url1"),
                CartItem(id = 2L, productId = 20L, name = "Teclado", price = 500, qty = 1, imageUrl = "url2")
            )
            val resp = CartResponse(userId = 7L, items = items, totalItems = 3, subtotal = 1100.0)

            coEvery { api.getCart(7L) } returns resp

            val result = api.getCart(7L)
            result.userId shouldBe 7L
            result.totalItems shouldBe 3
            result.subtotal shouldBe 1100.0
            coVerify(exactly = 1) { api.getCart(7L) }
        }

        it("CouponsApi.getCoupon retorna cupón simulado por código") {
            val api = mockk<CouponsApi>()
            val coupon = CouponDTO(id = 1L, code = "PROMO10", discount = 10, active = true)

            coEvery { api.getCoupon("PROMO10") } returns coupon

            val result = api.getCoupon("PROMO10")
            result.discount shouldBe 10
            result.active shouldBe true
            coVerify(exactly = 1) { api.getCoupon("PROMO10") }
        }
    }
})
