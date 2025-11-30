package com.example.levelup.viewmodel

import com.example.levelup.data.session.SessionManager
import com.example.levelup.data.session.UserSession
import io.kotest.core.spec.style.AnnotationSpec
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class TopBarViewModelTest : AnnotationSpec() {

    private lateinit var sessionManager: SessionManager
    private lateinit var viewModel: TopBarViewModel

    private val testUser = UserSession(
        id = 10,
        name = "Chapsui",
        email = "chapsui@mail.com",
        role = "USER"
    )

    @BeforeEach
    fun setup() = runTest {
        Dispatchers.setMain(StandardTestDispatcher())
        sessionManager = mockk()
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // ============================================================
    // INIT CARGA USUARIO
    // ============================================================

    @Test
    fun `init carga usuario desde SessionManager`() = runTest {
        // Mock: init debe obtener este usuario
        every { sessionManager.getCurrentUser() } returns testUser

        viewModel = TopBarViewModel(sessionManager)
        advanceUntilIdle()

        assertEquals(testUser, viewModel.currentUser.value)
    }

    @Test
    fun `init deja usuario en null si SessionManager devuelve null`() = runTest {
        every { sessionManager.getCurrentUser() } returns null

        viewModel = TopBarViewModel(sessionManager)
        advanceUntilIdle()

        assertNull(viewModel.currentUser.value)
    }

    // ============================================================
    // refreshUser()
    // ============================================================

    @Test
    fun `refreshUser actualiza currentUser correctamente`() = runTest {
        // Primer valor (init)
        every { sessionManager.getCurrentUser() } returns null

        viewModel = TopBarViewModel(sessionManager)
        advanceUntilIdle()

        assertNull(viewModel.currentUser.value)

        // Mock nuevo usuario
        every { sessionManager.getCurrentUser() } returns testUser

        // Ejecutar refresh
        viewModel.refreshUser()

        assertEquals(testUser, viewModel.currentUser.value)
    }

    @Test
    fun `refreshUser establece null cuando sessionManager devuelve null`() = runTest {
        every { sessionManager.getCurrentUser() } returns testUser

        viewModel = TopBarViewModel(sessionManager)
        advanceUntilIdle()

        assertEquals(testUser, viewModel.currentUser.value)

        // Ahora SessionManager devuelve null
        every { sessionManager.getCurrentUser() } returns null

        viewModel.refreshUser()

        assertNull(viewModel.currentUser.value)
    }
}
