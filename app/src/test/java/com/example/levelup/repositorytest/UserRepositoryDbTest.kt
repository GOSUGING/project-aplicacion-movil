package com.example.levelup.repositorytest

import com.example.levelup.data.UserDao
import com.example.levelup.data.UserEntity
import com.example.levelup.data.UserRepository
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk

class UserRepositoryDbTest : DescribeSpec({

    describe("UserRepository login y register con DAO simulado") {

        it("register éxito retorna id") {
            val dao = mockk<UserDao>()
            val repo = UserRepository(dao)
            val user = UserEntity(nombre = "U", email = "u@e.com", fechaNacimiento = "2000-01-01", passwordHash = "pass")

            coEvery { dao.insert(any()) } returns 42L

            val res = repo.register(user)
            res.isSuccess shouldBe true
            res.getOrNull() shouldBe 42L
        }

        it("register failure por excepción del DAO") {
            val dao = mockk<UserDao>()
            val repo = UserRepository(dao)
            val user = UserEntity(nombre = "U", email = "u@e.com", fechaNacimiento = "2000-01-01", passwordHash = "pass")

            coEvery { dao.insert(any()) } throws RuntimeException("collision")

            val res = repo.register(user)
            res.isFailure shouldBe true
        }

        it("login falla usuario no encontrado") {
            val dao = mockk<UserDao>()
            val repo = UserRepository(dao)

            coEvery { dao.getByEmail("user@example.com") } returns null

            val res = repo.login("user@example.com", "pw")
            res.isFailure shouldBe true
        }

        it("login falla contraseña incorrecta") {
            val dao = mockk<UserDao>()
            val repo = UserRepository(dao)
            val entity = UserEntity(id = 1L, nombre = "U", email = "user@example.com", fechaNacimiento = "2000-01-01", passwordHash = "otra")

            coEvery { dao.getByEmail("user@example.com") } returns entity

            val res = repo.login("user@example.com", "pw")
            res.isFailure shouldBe true
        }

        it("login éxito retorna entidad") {
            val dao = mockk<UserDao>()
            val repo = UserRepository(dao)
            val entity = UserEntity(id = 1L, nombre = "U", email = "user@example.com", fechaNacimiento = "2000-01-01", passwordHash = "pw")

            coEvery { dao.getByEmail("user@example.com") } returns entity

            val res = repo.login("user@example.com", "pw")
            res.isSuccess shouldBe true
            res.getOrNull()!!.id shouldBe 1L
        }
    }
})

