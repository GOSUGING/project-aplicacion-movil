package com.example.levelup.viewmodel

import com.example.levelup.data.dto.UserDTO
import com.example.levelup.data.network.UserApi
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AdminUsersViewModelCombinedActionsTest {
    private val dispatcher = UnconfinedTestDispatcher()

    @Test
    fun load_toggle_delete_callsApis() = runTest(dispatcher) {
        val api = mockk<UserApi>()
        coEvery { api.getUsers() } returns listOf(UserDTO(1L, "Ana", "a@a.com", null, null, "ADMIN"))
        coEvery { api.updateUserRole(1L, any()) } returns UserDTO(1L, "Ana", "a@a.com", null, null, "USER")
        coEvery { api.deleteUser(1L) } returns Unit

        val vm = AdminUsersViewModel(api)
        vm.loadUsers()
        advanceUntilIdle()

        vm.toggleRole(1L)
        advanceUntilIdle()

        vm.deleteUser(1L)
        advanceUntilIdle()

        coVerify(exactly = 1) { api.updateUserRole(1L, any()) }
        coVerify(exactly = 1) { api.deleteUser(1L) }
        coVerify(atLeast = 1) { api.getUsers() }
    }
}

