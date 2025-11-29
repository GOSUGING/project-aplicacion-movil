package com.example.levelup.MethodTest

import com.example.levelup.data.dto.RegisterResponse
import com.example.levelup.data.network.AuthApi
import com.example.levelup.viewmodel.RegisterViewModel
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk

class RegisterViewModelTest : DescribeSpec({

    describe("RegisterViewModel validaciones y submit") {

        it("onBlur de email inválido setea error") {
            val api = mockk<AuthApi>()
            val vm = RegisterViewModel(api)

            vm.onChange("email", "invalido")
            vm.onBlur("email")

            vm.ui.value.error shouldBe "Ingresa un correo electrónico válido"
        }

        it("contraseñas distintas setea error") {
            val api = mockk<AuthApi>()
            val vm = RegisterViewModel(api)

            vm.onChange("password", "Abcdef1!")
            vm.onChange("password2", "Otra")
            vm.onBlur("password2")

            vm.ui.value.error shouldBe "Las contraseñas no coinciden"
        }

        // Nota: omitimos pruebas de submit() porque usa viewModelScope (Dispatchers.Main)
    }
})
