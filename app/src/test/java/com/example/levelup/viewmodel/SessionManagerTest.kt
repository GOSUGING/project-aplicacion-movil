package com.example.levelup.viewmodel

import android.content.Context
import android.content.SharedPreferences
import com.example.levelup.data.session.SessionManager
import com.example.levelup.data.session.UserSession
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class SessionManagerTest : DescribeSpec({

    describe("SessionManager estado en memoria") {

        it("setCurrentUser y getCurrentUser funcionan") {
            val prefs = mockk<SharedPreferences>(relaxed = true)
            val ctx = mockk<Context>()
            every { ctx.getSharedPreferences("user_session", Context.MODE_PRIVATE) } returns prefs

            val sm = SessionManager(ctx)
            val user = UserSession(1L, "U", "u@e.com", "USER")

            sm.setCurrentUser(user)

            sm.getCurrentUser()?.email shouldBe "u@e.com"
        }

        it("logout borra usuario en memoria") {
            val prefs = mockk<SharedPreferences>(relaxed = true)
            val ctx = mockk<Context>()
            every { ctx.getSharedPreferences("user_session", Context.MODE_PRIVATE) } returns prefs

            val sm = SessionManager(ctx)
            sm.setCurrentUser(UserSession(1L, "U", "u@e.com", "USER"))
            sm.logout()

            sm.getCurrentUser() shouldBe null
        }
    }
})

