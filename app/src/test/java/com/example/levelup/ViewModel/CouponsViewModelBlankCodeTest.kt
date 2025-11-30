package com.example.levelup.ViewModel

import com.example.levelup.data.repository.CouponsRepository
import com.example.levelup.viewmodel.CouponsViewModel
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class CouponsViewModelBlankCodeTest {
    @Test
    fun fetchCoupon_blank_setsErrorEarly() {
        val repo = mockk<CouponsRepository>()
        val vm = CouponsViewModel(repo)

        vm.fetchCoupon("")

        assertEquals("Ingresa un cupón", vm.ui.value.error)
        assertNull(vm.ui.value.coupon)
        assertFalse(vm.ui.value.isLoading)
    }
}

