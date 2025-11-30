package com.example.levelup.uiscreens

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.rememberNavController
import com.example.levelup.data.dto.UserDTO
import com.example.levelup.data.network.UserApi
import com.example.levelup.viewmodel.AdminUsersViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AdminUsersScreenListShowsDetailsTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun list_renders_user_fields_and_actions() = runTest(UnconfinedTestDispatcher()) {
        val api = mockk<UserApi>()
        coEvery { api.getUsers() } returns listOf(
            UserDTO(1L, "Bob", "bob@example.com", null, null, "ADMIN")
        )
        coEvery { api.updateUserRole(any(), any()) } returns UserDTO(1L, "Bob", "bob@example.com", null, null, "USER")
        coEvery { api.deleteUser(any()) } returns Unit

        val vm = AdminUsersViewModel(api)

        compose.setContent {
            val nav = rememberNavController()
            com.example.levelup.ui.screens.AdminUsersScreen(navController = nav, vm = vm)
        }

        advanceUntilIdle()

        compose.onNodeWithText("Bob").assertIsDisplayed()
        compose.onNodeWithText("Email: bob@example.com").assertIsDisplayed()
        compose.onNodeWithText("Rol: ADMIN").assertIsDisplayed()
        compose.onNodeWithText("Cambiar Rol").assertIsDisplayed()
        compose.onNodeWithText("Eliminar").assertIsDisplayed()
    }
}

