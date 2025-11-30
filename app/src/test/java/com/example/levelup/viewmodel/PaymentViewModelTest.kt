package com.example.levelup.viewmodel

import com.example.levelup.data.dto.*
import com.example.levelup.data.repository.PaymentRepository
import com.example.levelup.model.CartItem
import io.kotest.core.spec.style.AnnotationSpec
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class PaymentViewModelTest : AnnotationSpec() {

    private lateinit var repo: PaymentRepository
    private lateinit var viewModel: PaymentViewModel

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
        repo = mockk()
        viewModel = PaymentViewModel(repo)
    }

    @AfterEach
    fun cleanup() {
        Dispatchers.resetMain()
    }

    // =============================
    // Helpers con TUS modelos reales
    // =============================

    private fun fakePayment(id: Long = 1L) = PaymentDTO(
        id = id,
        cantidad = 2,
        total = 30000,
        estado = "APROBADO",
        fecha = "2025-01-01",
        nombreUsuario = "Chapsui",
        direccionEnvio = "Mi casa",
        userId = 10,
        rawPayload = "{}"
    )

    private fun fakeCheckoutResponse() = CheckoutResponseDTO(
        message = "Pago exitoso"
    )

    private fun fakeCardPayment() = CardPaymentDTO(
        cardNumber = "1234567890123456",
        cardHolder = "CHAPSUI",
        expirationMonth = 12,
        expirationYear = 2030,
        cvv = "123"
    )

    private fun fakeCartItems() = listOf(
        CartItem(
            id = 1,
            productId = 10,
            name = "PS5",
            price = 5000,
            qty = 2,
            imageUrl = "img"
        ),
        CartItem(
            id = 2,
            productId = 20,
            name = "Xbox",
            price = 20000,
            qty = 1,
            imageUrl = "img"
        )
    )

    // =============================
    // TEST loadPayments()
    // =============================

    @Test
    fun `loadPayments carga lista correctamente`() = runTest {
        val list = listOf(fakePayment(1), fakePayment(2))

        coEvery { repo.getPayments() } returns Result.success(list)

        viewModel.loadPayments()
        advanceUntilIdle()

        assertEquals(2, viewModel.ui.value.payments.size)
        assertNull(viewModel.ui.value.error)
        assertEquals(false, viewModel.ui.value.loading)
    }

    @Test
    fun `loadPayments muestra error cuando falla`() = runTest {
        coEvery { repo.getPayments() } returns Result.failure(Exception("Error API"))

        viewModel.loadPayments()
        advanceUntilIdle()

        assertNotNull(viewModel.ui.value.error)
        assertTrue(viewModel.ui.value.error!!.contains("Error API"))
        assertEquals(false, viewModel.ui.value.loading)
    }

    // =============================
    // TEST checkout()
    // =============================

    @Test
    fun `checkout éxito actualiza successMessage y llama onSuccess`() = runTest {
        val items = fakeCartItems()

        coEvery {
            repo.checkout(
                userId = 1,
                items = any(),
                total = 30000,
                nombreUsuario = "Chapsui",
                direccionEnvio = "Mi casa",
                cardPaymentDTO = any()
            )
        } returns Result.success(fakeCheckoutResponse())

        var callbackInvoked = false

        viewModel.checkout(
            userId = 1,
            items = items,
            total = 30000,
            nombreUsuario = "Chapsui",
            direccionEnvio = "Mi casa",
            cardPaymentDTO = fakeCardPayment()
        ) { callbackInvoked = true }

        advanceUntilIdle()

        assertEquals("Pago exitoso", viewModel.ui.value.successMessage)
        assertNull(viewModel.ui.value.error)
        assertTrue(callbackInvoked)
        assertEquals(false, viewModel.ui.value.loading)
    }

    @Test
    fun `checkout error actualiza error y no llama onSuccess`() = runTest {
        val items = fakeCartItems()

        coEvery {
            repo.checkout(
                userId = any(),
                items = any(),
                total = any(),
                nombreUsuario = any(),
                direccionEnvio = any(),
                cardPaymentDTO = any()
            )
        } returns Result.failure(Exception("Error de pago"))

        var callbackInvoked = false

        viewModel.checkout(
            userId = 1,
            items = items,
            total = 30000,
            nombreUsuario = "Chapsui",
            direccionEnvio = "Mi casa",
            cardPaymentDTO = fakeCardPayment()
        ) { callbackInvoked = true }

        advanceUntilIdle()

        assertNotNull(viewModel.ui.value.error)
        assertTrue(viewModel.ui.value.error!!.contains("Error de pago"))
        assertEquals(false, viewModel.ui.value.loading)
        assertTrue(!callbackInvoked)
    }
}
