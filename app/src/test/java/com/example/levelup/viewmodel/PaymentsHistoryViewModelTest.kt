package com.example.levelup.viewmodel

import com.example.levelup.data.dto.PaymentDTO
import com.example.levelup.data.repository.PaymentRepository
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.coroutines.resume

@OptIn(ExperimentalCoroutinesApi::class)
class PaymentsHistoryViewModelTest : AnnotationSpec() {

    private lateinit var repo: PaymentRepository
    private lateinit var viewModel: PaymentsHistoryViewModel
    private val testDispatcher = StandardTestDispatcher()

    // --------------------------
    // Helper DTO
    // --------------------------
    private fun fakePayment() = PaymentDTO(
        id = 1L,
        cantidad = 2,
        total = 19990,
        estado = "COMPLETADO",
        fecha = "2025-05-10",
        nombreUsuario = "CHAPSUI",
        direccionEnvio = "Santiago",
        userId = 10L,
        rawPayload = "{}"
    )

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repo = mockk(relaxed = true)
        viewModel = PaymentsHistoryViewModel(repo)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // ======================================================
    // 1) CARGA EXITOSA
    // ======================================================
    @Test
    fun `loadPayments actualiza payments correctamente`() = runTest {
        val list = listOf(fakePayment())

        coEvery { repo.getPayments() } returns Result.success(list)

        viewModel.loadPayments()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.ui.value.loading shouldBe false
        viewModel.ui.value.payments shouldBe list
        viewModel.ui.value.error shouldBe null
    }

    // ======================================================
    // 2) MANEJO DE ERROR
    // ======================================================
    @Test
    fun `loadPayments maneja error correctamente`() = runTest {
        coEvery { repo.getPayments() } returns Result.failure(Exception("Error de servidor"))

        viewModel.loadPayments()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.ui.value.loading shouldBe false
        viewModel.ui.value.payments shouldBe emptyList()
        viewModel.ui.value.error shouldBe "Error de servidor"
    }

    // ======================================================
    // 3) NO ejecutar 2 veces cuando loading = true
    // ======================================================
    @Test
    fun `loadPayments no ejecuta otra llamada si ya está cargando`() = runTest {
        // Primera llamada queda suspendida → loading = true
        coEvery { repo.getPayments() } coAnswers {
            suspendCancellableCoroutine { }
        }

        // Ejecutar primera llamada
        viewModel.loadPayments()

        // Avanzar corrutina → loading = true aplicado al UI
        testDispatcher.scheduler.advanceUntilIdle()

        // Segunda llamada NO debe ejecutar repo.getPayments() otra vez
        viewModel.loadPayments()

        viewModel.ui.value.loading shouldBe true

        // Debe haberse llamado solo UNA VEZ
        coVerify(exactly = 1) { repo.getPayments() }
    }
}
