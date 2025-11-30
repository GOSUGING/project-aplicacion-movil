package com.example.levelup.RepositoryTest

import com.example.levelup.data.dto.CouponDTO
import com.example.levelup.data.network.CouponsApi
import com.example.levelup.data.repository.CouponsRepository
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk

class CouponsRepositoryTest : DescribeSpec({

    describe("CouponsRepository.findCoupon") {

        it("retorna success con cupón válido") {
            val api = mockk<CouponsApi>()
            val repo = CouponsRepository(api)
            val coupon = CouponDTO(1L, "PROMO10", 10, true)

            coEvery { api.getCoupon("PROMO10") } returns coupon

            val res = repo.findCoupon("PROMO10")
            res.isSuccess shouldBe true
            res.getOrNull()!!.code shouldBe "PROMO10"
        }

        it("retorna failure ante excepción del API") {
            val api = mockk<CouponsApi>()
            val repo = CouponsRepository(api)

            coEvery { api.getCoupon(any()) } throws RuntimeException("no encontrado")

            val res = repo.findCoupon("X")
            res.isFailure shouldBe true
        }
    }
})

