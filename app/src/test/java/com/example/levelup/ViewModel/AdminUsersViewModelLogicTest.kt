package com.example.levelup.ViewModel

import com.example.levelup.data.dto.UserDTO
import com.example.levelup.data.network.UserApi
import com.example.levelup.viewmodel.AdminUsersViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

@OptIn(ExperimentalCoroutinesApi::class)
class AdminUsersViewModelLogicTest {
    private val dispatcher = UnconfinedTestDispatcher()

    @Test
    fun toggleRole_callsUpdateUserRole() = kotlinx.coroutines.test.runTest {
        val api = mockk<UserApi>()
        coEvery { api.getUsers() } returns listOf(UserDTO(1L, "Admin", "a@a.com", null, null, "ADMIN"))
        coEvery { api.updateUserRole(1L, any()) } returns UserDTO(1L, "Admin", "a@a.com", null, null, "USER")
        val vm = AdminUsersViewModel(api)
        vm.loadUsers()
        advanceUntilIdle()
        vm.toggleRole(1L)
        advanceUntilIdle()
        coVerify { api.updateUserRole(1L, any()) }
    }

    @Test
    fun deleteUser_callsApiAndReloads() = kotlinx.coroutines.test.runTest {
        val api = mockk<UserApi>()
        coEvery { api.getUsers() } returns emptyList()
        coEvery { api.deleteUser(2L) } returns Unit
        val vm = AdminUsersViewModel(api)
        vm.deleteUser(2L)
        advanceUntilIdle()
        coVerify { api.deleteUser(2L) }
        coVerify { api.getUsers() }
    }
}
