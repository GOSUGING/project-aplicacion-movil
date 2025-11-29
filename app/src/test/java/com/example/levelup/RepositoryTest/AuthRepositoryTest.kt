package com.example.levelup.RepositoryTest

import com.example.levelup.data.dto.LoginRequest
import com.example.levelup.data.dto.LoginResponse
import com.example.levelup.data.network.AuthApi
import com.example.levelup.data.repository.UserRepository
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk

class AuthRepositoryTest : DescribeSpec({

    describe("UserRepository (Auth) login") {

        it("login éxito retorna LoginResponse") {
            val api = mockk<AuthApi>()
            val repo = UserRepository(api)

            val resp = LoginResponse(id = 1L, name = "U", email = "u@e.com", role = "USER")
            coEvery { api.login(LoginRequest("u@e.com", "pw")) } returns resp

            val r = repo.login("u@e.com", "pw")
            r.isSuccess shouldBe true
            r.getOrNull()!!.id shouldBe 1L
        }

        it("login failure captura excepción del API") {
            val api = mockk<AuthApi>()
            val repo = UserRepository(api)

            coEvery { api.login(any()) } throws RuntimeException("bad creds")

            val r = repo.login("u@e.com", "pw")
            r.isFailure shouldBe true
        }
    }
})

