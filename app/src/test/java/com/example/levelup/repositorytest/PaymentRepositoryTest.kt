package com.example.levelup.repositorytest

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

class PaymentRepositoryTest : DescribeSpec({

    describe("PaymentRepository checkout simulaciones y real") {

        it("tarjeta 1111 retorna éxito simulado") {
            val api = mockk<PaymentApi>()
            val repo = PaymentRepository(api)

            val res = repo.checkout(
                userId = 1L,
                items = listOf(CheckoutItem(10L, 1, 300)),
                total = 300,
                nombreUsuario = "U",
                direccionEnvio = "Dir",
                cardPaymentDTO = CardPaymentDTO("4111111111111111", "U", 12, 2030, "123")
            )

            res.isSuccess shouldBe true
            coVerify(exactly = 0) { api.checkout(any()) }
        }

        it("tarjeta 0000 retorna failure simulado") {
            val api = mockk<PaymentApi>()
            val repo = PaymentRepository(api)

            val res = repo.checkout(
                userId = 1L,
                items = listOf(CheckoutItem(10L, 1, 300)),
                total = 300,
                nombreUsuario = "U",
                direccionEnvio = "Dir",
                cardPaymentDTO = CardPaymentDTO("4000000000000000", "U", 12, 2030, "123")
            )

            res.isFailure shouldBe true
            coVerify(exactly = 0) { api.checkout(any()) }
        }

        it("tarjeta normal hace POST real y retorna OK") {
            val api = mockk<PaymentApi>()
            val repo = PaymentRepository(api)

            coEvery { api.checkout(any()) } returns CheckoutResponseDTO("OK")

            val res = repo.checkout(
                userId = 1L,
                items = listOf(CheckoutItem(10L, 1, 300)),
                total = 300,
                nombreUsuario = "U",
                direccionEnvio = "Dir",
                cardPaymentDTO = CardPaymentDTO("5555555555554444", "U", 12, 2030, "123")
            )

            res.isSuccess shouldBe true
            coVerify(exactly = 1) { api.checkout(any()) }
        }
    }
})

