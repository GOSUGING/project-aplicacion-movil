package com.example.levelup.ViewModel

import com.example.levelup.data.ProductRepository
import com.example.levelup.data.network.ProductApi
import com.example.levelup.viewmodel.AdminViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AdminViewModelRoleLogicTest {
    private val dispatcher = UnconfinedTestDispatcher()

    @Test
    fun setRole_uppercases_and_null_clears() = runTest(dispatcher) {
        val repo = mockk<ProductRepository>()
        val api = mockk<ProductApi>()
        coEvery { api.getProducts() } returns emptyList()

        val vm = AdminViewModel(repo, api)
        advanceUntilIdle()

        vm.setRole("admin")
        assertEquals("ADMIN", vm.ui.value.role)

        vm.setRole(null)
        assertEquals("", vm.ui.value.role)
    }
}

