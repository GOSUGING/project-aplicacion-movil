package com.example.levelup.ViewModel

import com.example.levelup.data.repository.UserRepository
import com.example.levelup.data.session.SessionManager
import com.example.levelup.viewmodel.LoginViewModel
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelBasicsTest {
    private val dispatcher = UnconfinedTestDispatcher()

    @Test
    fun emptyFields_setError() = runTest(dispatcher) {
        val repo = mockk<UserRepository>()
        val session = mockk<SessionManager>(relaxed = true)
        val vm = LoginViewModel(repo, session)
        vm.onChange("email", "")
        vm.onChange("password", "")
        vm.login { }
        assertEquals("Completa todos los campos", vm.ui.value.error)
    }
}
