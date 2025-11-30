package com.example.levelup.ViewModel

import com.example.levelup.data.network.AuthApi
import com.example.levelup.viewmodel.RegisterViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import io.mockk.mockk

@OptIn(ExperimentalCoroutinesApi::class)
class RegisterViewModelFieldBlurTest {
    private val dispatcher = UnconfinedTestDispatcher()

    @Test
    fun nombreBlank_setsError() = runTest(dispatcher) {
        val vm = RegisterViewModel(mockk<AuthApi>())
        vm.onChange("nombre", "")
        vm.onBlur("nombre")
        assertTrue((vm.ui.value.error ?: "").isNotBlank())
    }

    @Test
    fun phoneBlank_setsError() = runTest(dispatcher) {
        val vm = RegisterViewModel(mockk<AuthApi>())
        vm.onChange("phone", "")
        vm.onBlur("phone")
        assertTrue((vm.ui.value.error ?: "").isNotBlank())
    }

    @Test
    fun addressBlank_setsError() = runTest(dispatcher) {
        val vm = RegisterViewModel(mockk<AuthApi>())
        vm.onChange("address", "")
        vm.onBlur("address")
        assertTrue((vm.ui.value.error ?: "").isNotBlank())
    }

    @Test
    fun passwordWeak_setsError() = runTest(dispatcher) {
        val vm = RegisterViewModel(mockk<AuthApi>())
        vm.onChange("password", "abcdefg")
        vm.onBlur("password")
        assertTrue((vm.ui.value.error ?: "").isNotBlank())
    }
}

