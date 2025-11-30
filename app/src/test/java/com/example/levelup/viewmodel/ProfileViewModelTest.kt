package com.example.levelup.viewmodel

import com.example.levelup.data.UserRepository
import com.example.levelup.data.session.SessionManager
import com.example.levelup.data.session.UserSession
import io.kotest.core.spec.style.AnnotationSpec
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileViewModelTest : AnnotationSpec() {

    private lateinit var repo: UserRepository
    private lateinit var sessionManager: SessionManager
    private lateinit var viewModel: ProfileViewModel

    // Usuario simulado REAL según tu modelo
    private val testUser = UserSession(
        id = 1,
        name = "Chapsui",
        email = "chapsui@mail.com",
        role = "ADMIN"
    )

    @BeforeEach
    fun setup() = runTest {
        Dispatchers.setMain(StandardTestDispatcher())

        repo = mockk()
        sessionManager = mockk()

        // Cuando el ViewModel se crea, init { loadCurrentUser() } llama a esto
        coEvery { sessionManager.getCurrentUser() } returns testUser

        viewModel = ProfileViewModel(repo, sessionManager)

        // Esperar a que termine el init del ViewModel
        advanceUntilIdle()
    }

    @AfterEach
    fun cleanup() {
        Dispatchers.resetMain()
    }

    // =============================
    // TEST: loadCurrentUser()
    // =============================

    @Test
    fun `loadCurrentUser carga los datos del usuario correctamente`() = runTest {
        assertEquals("Chapsui", viewModel.uiState.name)
        assertEquals("chapsui@mail.com", viewModel.uiState.email)
        assertEquals("ADMIN", viewModel.uiState.role)
        assertEquals("", viewModel.uiState.address)
        assertEquals("", viewModel.uiState.phone)
    }

    // =============================
    // TEST: onFieldChange()
    // =============================

    @Test
    fun `onFieldChange actualiza nombre`() {
        viewModel.onFieldChange("name", "NuevoNombre")

        assertEquals("NuevoNombre", viewModel.uiState.name)
    }

    @Test
    fun `onFieldChange actualiza email`() {
        viewModel.onFieldChange("email", "nuevo@mail.com")

        assertEquals("nuevo@mail.com", viewModel.uiState.email)
    }

    @Test
    fun `onFieldChange actualiza newsletter`() {
        viewModel.onFieldChange("newsletter", true)

        assertTrue(viewModel.uiState.newsletter)
    }

    // =============================
    // TEST: saveProfile()
    // =============================

    @Test
    fun `saveProfile muestra error si email es inválido`() {
        viewModel.onFieldChange("email", "correo_malo")

        viewModel.saveProfile()

        assertEquals("Nombre o email inválidos", viewModel.uiState.errorMessage)
    }

    @Test
    fun `saveProfile actualiza correctamente si los datos son válidos`() = runTest {
        coEvery { sessionManager.getCurrentUser() } returns testUser
        coEvery { sessionManager.setCurrentUser(any()) } just runs

        viewModel.onFieldChange("name", "Nuevo User")
        viewModel.onFieldChange("email", "nuevo@mail.com")

        viewModel.saveProfile()
        advanceUntilIdle()

        assertEquals("Perfil actualizado correctamente", viewModel.uiState.successMessage)
        assertNull(viewModel.uiState.errorMessage)

        // Validar que guardamos el usuario actualizado
        coVerify {
            sessionManager.setCurrentUser(
                match {
                    it.name == "Nuevo User" &&
                            it.email == "nuevo@mail.com" &&
                            it.role == "ADMIN"
                }
            )
        }
    }

    // =============================
    // TEST: logout()
    // =============================

    @Test
    fun `logout llama a sessionManager logout`() {
        every { sessionManager.logout() } just runs

        viewModel.logout()

        coVerify { sessionManager.logout() }
    }
}
