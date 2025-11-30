package com.example.levelup.UiScreens

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.rememberNavController
import com.example.levelup.data.dto.UserDTO
import com.example.levelup.data.network.UserApi
import com.example.levelup.ui.screens.AdminUsersScreen
import com.example.levelup.viewmodel.AdminUsersViewModel
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test

class AdminUsersScreenListRendersTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun renders_title_and_user_item() {
        val api = mockk<UserApi>()
        coEvery { api.getUsers() } returns listOf(UserDTO(1L, "Bob", "bob@example.com", null, null, "USER"))
        val vm = AdminUsersViewModel(api)

        compose.setContent {
            val nav = rememberNavController()
            AdminUsersScreen(navController = nav, vm = vm)
        }

        compose.onNodeWithText("GESTIÓN DE USUARIOS").assertIsDisplayed()
        compose.onNodeWithText("Bob").assertIsDisplayed()
        compose.onNodeWithText("Email: bob@example.com").assertIsDisplayed()
        compose.onNodeWithText("Cambiar Rol").assertIsDisplayed()
        compose.onNodeWithText("Eliminar").assertIsDisplayed()
    }
}

