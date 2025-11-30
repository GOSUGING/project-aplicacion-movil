package com.example.levelup.uiscreens

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.levelup.data.repository.PaymentRepository
import com.example.levelup.viewmodel.PaymentsHistoryViewModel
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test

class PaymentsHistoryBackButtonClickableTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun back_button_triggers_onBack_callback() {
        val repo = mockk<PaymentRepository>()
        coEvery { repo.getPayments() } returns Result.success(emptyList())
        val vm = PaymentsHistoryViewModel(repo)

        var backClicks = 0

        compose.setContent {
            com.example.levelup.ui.screens.PaymentsHistoryScreen(viewModel = vm, onBack = { backClicks++ })
        }

        compose.onNodeWithText("VOLVER").assertIsDisplayed().performClick()
        assert(backClicks == 1)
    }
}

