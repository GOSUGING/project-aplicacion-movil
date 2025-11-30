package com.example.levelup.uiscreens

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
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
class AdminUsersScreenRoleToggleFlowTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun toggling_role_updates_label_after_refresh() = runTest(UnconfinedTestDispatcher()) {
        val api = mockk<UserApi>()

        coEvery { api.getUsers() } returnsMany listOf(
            listOf(UserDTO(1L, "Bob", "bob@example.com", null, null, "ADMIN")),
            listOf(UserDTO(1L, "Bob", "bob@example.com", null, null, "USER"))
        )
        coEvery { api.updateUserRole(1L, any()) } returns UserDTO(1L, "Bob", "bob@example.com", null, null, "USER")

        val vm = AdminUsersViewModel(api)

        compose.setContent {
            val nav = rememberNavController()
            com.example.levelup.ui.screens.AdminUsersScreen(navController = nav, vm = vm)
        }

        advanceUntilIdle()

        compose.onNodeWithText("Rol: ADMIN").assertIsDisplayed()
        compose.onNodeWithText("Cambiar Rol").performClick()

        advanceUntilIdle()

        compose.onNodeWithText("Rol: USER").assertIsDisplayed()
    }
}

