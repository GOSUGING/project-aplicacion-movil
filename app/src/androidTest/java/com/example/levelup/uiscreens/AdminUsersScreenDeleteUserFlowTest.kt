package com.example.levelup.uiscreens

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
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
class AdminUsersScreenDeleteUserFlowTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun deleting_user_removes_it_from_list_after_refresh() = runTest(UnconfinedTestDispatcher()) {
        val api = mockk<UserApi>()

        coEvery { api.getUsers() } returnsMany listOf(
            listOf(UserDTO(2L, "Eve", "eve@example.com", null, null, "USER")),
            emptyList()
        )
        coEvery { api.deleteUser(2L) } returns Unit

        val vm = AdminUsersViewModel(api)

        compose.setContent {
            val nav = rememberNavController()
            com.example.levelup.ui.screens.AdminUsersScreen(navController = nav, vm = vm)
        }

        advanceUntilIdle()

        compose.onNodeWithText("Eve").assertIsDisplayed()
        compose.onNodeWithText("Eliminar").performClick()

        advanceUntilIdle()

        val nodes = compose.onAllNodesWithText("Eve").fetchSemanticsNodes()
        assert(nodes.isEmpty())
    }
}
