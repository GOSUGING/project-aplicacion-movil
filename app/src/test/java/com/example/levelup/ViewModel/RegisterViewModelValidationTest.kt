package com.example.levelup.ViewModel

import com.example.levelup.data.network.AuthApi
import com.example.levelup.viewmodel.RegisterViewModel
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

@OptIn(ExperimentalCoroutinesApi::class)
class RegisterViewModelValidationTest {
    private val dispatcher = UnconfinedTestDispatcher()

    @Test
    fun emailInvalid_setsErrorOnBlur() = runTest(dispatcher) {
        val vm = RegisterViewModel(mockk<AuthApi>())
        vm.onChange("email", "bad-email")
        vm.onBlur("email")
        assertTrue((vm.ui.value.error ?: "").contains("correo"))
    }

    @Test
    fun passwordsMismatch_setsError() = runTest(dispatcher) {
        val vm = RegisterViewModel(mockk<AuthApi>())
        vm.onChange("password", "Abcdef1!")
        vm.onChange("password2", "Abcdef2!")
        vm.onBlur("password2")
        assertTrue((vm.ui.value.error ?: "").contains("no coinciden"))
    }

    @Test
    fun underAge_setsError() = runTest(dispatcher) {
        val vm = RegisterViewModel(mockk<AuthApi>())
        vm.onChange("fechaNacimiento", "2015-01-01")
        vm.onBlur("fechaNacimiento")
        assertTrue((vm.ui.value.error ?: "").contains("mayor de 18"))
    }
}
