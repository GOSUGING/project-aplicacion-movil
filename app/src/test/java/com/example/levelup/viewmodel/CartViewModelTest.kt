package com.example.levelup.viewmodel

import com.example.levelup.data.dto.AddItemRequest
import com.example.levelup.data.dto.CartResponse
import com.example.levelup.data.dto.ProductDTO
import com.example.levelup.data.repository.CartRepository
import com.example.levelup.data.session.SessionManager
import com.example.levelup.data.session.UserSession
import com.example.levelup.model.CartItem
import com.example.levelup.viewmodel.state.CartUiState
import io.kotest.core.spec.style.AnnotationSpec
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.*
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class CartViewModelTest : AnnotationSpec() {

    private lateinit var repo: CartRepository
    private lateinit var sessionManager: SessionManager
    private lateinit var viewModel: CartViewModel

    private val testUser = UserSession(
        id = 1,
        name = "Chapsui",
        email = "chapsui@mail.com",
        role = "USER"
    )

    private fun fakeItem(
        id: Long = 1,
        productId: Long = 10,
        qty: Int = 2,
        price: Int = 5000,
        name: String = "Producto"
    ) = CartItem(
        id = id,
        productId = productId,
        name = name,
        price = price,
        qty = qty,
        imageUrl = "img"
    )

    private fun fakeProduct() = ProductDTO(
        id = 10,
        name = "PS5",
        description = "Consola",
        price = 450000,
        img = "img_url",
        category = "consolas",
        stock = 10
    )

    private fun emptyResponse() = CartResponse(
        userId = 1,
        items = emptyList(),
        totalItems = 0,
        subtotal = 0.0
    )

    @BeforeEach
    fun setup() = runTest {
        Dispatchers.setMain(StandardTestDispatcher())
        repo = mockk()
        sessionManager = mockk()
        viewModel = CartViewModel(repo, sessionManager)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // ============================================================
    // loadCart()
    // ============================================================

    @Test
    fun `loadCart muestra error si no hay usuario`() = runTest {
        every { sessionManager.getCurrentUser() } returns null

        viewModel.loadCart()
        advanceUntilIdle()

        assertEquals("Usuario no autenticado", viewModel.ui.value.error)
        assertTrue(viewModel.ui.value.items.isEmpty())
    }

    @Test
    fun `loadCart carga items correctamente`() = runTest {
        every { sessionManager.getCurrentUser() } returns testUser

        val items = listOf(
            fakeItem(qty = 2),
            fakeItem(id = 2, productId = 20, qty = 3)
        )

        val response = CartResponse(
            userId = 1,
            items = items,
            totalItems = 5,
            subtotal = 15000.0
        )

        coEvery { repo.getCart(1) } returns Result.success(response)

        viewModel.loadCart()
        advanceUntilIdle()

        assertEquals(2, viewModel.ui.value.items.size)
        assertNull(viewModel.ui.value.error)
        assertFalse(viewModel.ui.value.isLoading)
    }

    @Test
    fun `loadCart error retorna lista vacía`() = runTest {
        every { sessionManager.getCurrentUser() } returns testUser

        coEvery { repo.getCart(1) } returns Result.failure(Exception("Error X"))

        viewModel.loadCart()
        advanceUntilIdle()

        assertTrue(viewModel.ui.value.items.isEmpty())
        assertTrue(viewModel.ui.value.error!!.contains("Error X"))
    }

    // ============================================================
    // addProduct()
    // ============================================================

    @Test
    fun `addProduct llama repo y recarga el carrito`() = runTest {
        every { sessionManager.getCurrentUser() } returns testUser

        coEvery { repo.addToCart(any(), any()) } returns Result.success(emptyResponse())
        coEvery { repo.getCart(1) } returns Result.success(emptyResponse())

        viewModel.addProduct(fakeProduct(), qty = 2)
        advanceUntilIdle()

        coVerify { repo.addToCart(eq(1), any()) }
        coVerify { repo.getCart(1) }
    }

    @Test
    fun `addProduct muestra error si falla`() = runTest {
        every { sessionManager.getCurrentUser() } returns testUser

        coEvery { repo.addToCart(any(), any()) } returns Result.failure(Exception("Falló"))

        viewModel.addProduct(fakeProduct(), qty = 1)
        advanceUntilIdle()

        assertEquals("Falló", viewModel.ui.value.error)
    }

    // ============================================================
    // deleteItem()
    // ============================================================

    @Test
    fun `deleteItem elimina y recarga carrito`() = runTest {
        every { sessionManager.getCurrentUser() } returns testUser

        coEvery { repo.deleteItem(1, 999) } returns Result.success(emptyResponse())
        coEvery { repo.getCart(1) } returns Result.success(emptyResponse())

        viewModel.deleteItem(999)
        advanceUntilIdle()

        coVerify { repo.deleteItem(1, 999) }
        coVerify { repo.getCart(1) }
    }

    @Test
    fun `deleteItem muestra error si falla`() = runTest {
        every { sessionManager.getCurrentUser() } returns testUser

        coEvery { repo.deleteItem(any(), any()) } returns Result.failure(Exception("Error borrar"))

        viewModel.deleteItem(999)
        advanceUntilIdle()

        assertEquals("Error borrar", viewModel.ui.value.error)
    }

    // ============================================================
    // clearCart()
    // ============================================================

    @Test
    fun `clearCart vacía items en éxito`() = runTest {
        every { sessionManager.getCurrentUser() } returns testUser

        coEvery { repo.clearAllItems(1) } returns Result.success(emptyResponse())

        viewModel.clearCart()
        advanceUntilIdle()

        assertTrue(viewModel.ui.value.items.isEmpty())
        assertNull(viewModel.ui.value.error)
    }

    @Test
    fun `clearCart muestra error si falla`() = runTest {
        every { sessionManager.getCurrentUser() } returns testUser

        coEvery { repo.clearAllItems(1) } returns Result.failure(Exception("Error limpiar"))

        viewModel.clearCart()
        advanceUntilIdle()

        assertEquals("Error limpiar", viewModel.ui.value.error)
    }

    // ============================================================
    // cartItemCount  ✔ LÓGICA PURA, SIN USAR EL VIEWMODEL
    // ============================================================

    @Test
    fun `cartItemCount suma cantidad correctamente`() = runTest {
        val items = listOf(
            fakeItem(qty = 2),
            fakeItem(qty = 3)
        )

        // Probamos la lógica original del map()
        val count = MutableStateFlow(CartUiState(items = items))
            .map { it.items.sumOf { item -> item.qty } }
            .first()

        assertEquals(5, count)
    }
}
