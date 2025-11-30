package com.example.levelup.viewmodel

import com.example.levelup.data.network.AuthApi
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class RegisterViewModelOnChangeTest {
    @Test
    fun onChange_updatesField_and_resetsError() {
        val api = mockk<AuthApi>()
        val vm = RegisterViewModel(api)

        vm.onChange("email", "a@a.com")

        assertEquals("a@a.com", vm.ui.value.email)
        assertNull(vm.ui.value.error)
    }
}

