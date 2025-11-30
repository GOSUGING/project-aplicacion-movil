package com.example.levelup.viewmodel

import com.example.levelup.data.dto.PaymentDTO
import com.example.levelup.data.network.PaymentApi
import io.kotest.core.spec.style.AnnotationSpec
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.jupiter.api.Assertions.assertFalse
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class SalesViewModelTest : AnnotationSpec() {

    private lateinit var paymentApi: PaymentApi
    private lateinit var viewModel: SalesViewModel

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

    @BeforeEach
    fun setup() = runTest {
        Dispatchers.setMain(StandardTestDispatcher())

        paymentApi = mockk()

        // Evitar que init llame a algo sin mocking
        coEvery { paymentApi.getAllPayments() } returns emptyList()

        viewModel = SalesViewModel(paymentApi)

        // Esperar a que init { loadSales() } se ejecute
        advanceUntilIdle()
    }

    @AfterEach
    fun cleanup() {
        Dispatchers.resetMain()
    }

    // ===========================================================
    // TEST INIT
    // ===========================================================

    @Test
    fun `init carga ventas correctamente usando loadSales`() = runTest {
        // Como mockeamos emptyList, el estado debe haber cargado eso
        assertEquals(0, viewModel.ui.value.sales.size)
        assertFalse(viewModel.ui.value.isLoading)
        assertNull(viewModel.ui.value.error)
    }

    // ===========================================================
    // TEST loadSales() éxito
    // ===========================================================

    @Test
    fun `loadSales carga ventas correctamente`() = runTest {
        val payments = listOf(fakePayment(1), fakePayment(2))

        coEvery { paymentApi.getAllPayments() } returns payments

        viewModel.loadSales()
        advanceUntilIdle()

        assertEquals(2, viewModel.ui.value.sales.size)
        assertNull(viewModel.ui.value.error)
        assertFalse(viewModel.ui.value.isLoading)
    }

    // ===========================================================
    // TEST loadSales() error
    // ===========================================================

    @Test
    fun `loadSales muestra error cuando falla`() = runTest {
        coEvery { paymentApi.getAllPayments() } throws Exception("Falló API")

        viewModel.loadSales()
        advanceUntilIdle()

        assertNotNull(viewModel.ui.value.error)
        assertTrue(viewModel.ui.value.error!!.contains("Falló API"))
        assertEquals(false, viewModel.ui.value.isLoading)
        assertEquals(0, viewModel.ui.value.sales.size)
    }
}
