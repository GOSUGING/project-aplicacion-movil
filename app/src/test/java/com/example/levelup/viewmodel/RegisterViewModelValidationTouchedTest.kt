package com.example.levelup.viewmodel

import com.example.levelup.data.network.AuthApi
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class RegisterViewModelValidationTouchedTest {
    @Test
    fun invalidEmail_onBlur_setsError() {
        val api = mockk<AuthApi>()
        val vm = RegisterViewModel(api)

        vm.onChange("email", "invalid")
        vm.onBlur("email")

        assertEquals("Ingresa un correo electrónico válido", vm.ui.value.error)
    }
}

