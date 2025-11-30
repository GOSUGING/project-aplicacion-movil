package com.example.levelup.apitest

import com.example.levelup.data.dto.CardPaymentDTO
import com.example.levelup.data.dto.CheckoutItem
import com.example.levelup.data.dto.CheckoutResponseDTO
import com.example.levelup.data.network.PaymentApi
import com.example.levelup.data.repository.PaymentRepository
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk

class PostViewModel : DescribeSpec({

    describe("POST checkout con fakepost y llamada real") {

        val api = mockk<PaymentApi>()
        val repo = PaymentRepository(api)

        val userId = 1L
        val items = listOf(CheckoutItem(productId = 10L, cantidad = 2, price = 500))
        val total = 1000
        val nombre = "Usuario"
        val direccion = "Calle 123"

        it("retorna éxito simulado cuando la tarjeta termina en 1111") {
            val card = CardPaymentDTO(
                cardNumber = "4111111111111111",
                cardHolder = "Usuario",
                expirationMonth = 12,
                expirationYear = 2030,
                cvv = "123"
            )

            val result = repo.checkout(userId, items, total, nombre, direccion, card)

            result.isSuccess shouldBe true
            result.getOrNull()?.message shouldBe "¡Simulación de Pago EXITOSA! Carrito vaciado (Local)."
            coVerify(exactly = 0) { api.checkout(any()) }
        }

        it("retorna fallo simulado cuando la tarjeta termina en 0000") {
            val card = CardPaymentDTO(
                cardNumber = "4000000000000000",
                cardHolder = "Usuario",
                expirationMonth = 12,
                expirationYear = 2030,
                cvv = "123"
            )

            val result = repo.checkout(userId, items, total, nombre, direccion, card)

            result.isFailure shouldBe true
            coVerify(exactly = 0) { api.checkout(any()) }
        }

        it("llama a POST real cuando la tarjeta no es de simulación") {
            val card = CardPaymentDTO(
                cardNumber = "5555555555554444",
                cardHolder = "Usuario",
                expirationMonth = 12,
                expirationYear = 2030,
                cvv = "123"
            )

            coEvery { api.checkout(any()) } returns CheckoutResponseDTO(message = "Pago OK")

            val result = repo.checkout(userId, items, total, nombre, direccion, card)

            result.isSuccess shouldBe true
            result.getOrNull()?.message shouldBe "Pago OK"
            coVerify(exactly = 1) { api.checkout(any()) }
        }
    }
})
