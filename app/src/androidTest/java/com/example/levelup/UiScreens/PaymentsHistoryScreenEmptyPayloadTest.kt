package com.example.levelup.UiScreens

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.levelup.data.dto.PaymentDTO
import com.example.levelup.data.repository.PaymentRepository
import com.example.levelup.viewmodel.PaymentsHistoryViewModel
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test

class PaymentsHistoryScreenEmptyPayloadTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun rendersEmptyProductsMessage_whenRawPayloadBlank() {
        val repo = mockk<PaymentRepository>()
        coEvery { repo.getPayments() } returns Result.success(
            listOf(PaymentDTO(1L, 1, 300L, "OK", "2024-01-01", "U", "Dir", 7L, ""))
        )
        val vm = PaymentsHistoryViewModel(repo)

        compose.setContent {
            com.example.levelup.ui.screens.PaymentsHistoryScreen(viewModel = vm, onBack = {})
        }

        compose.onNodeWithText("• Sin productos (payload vacío)").assertIsDisplayed()
    }
}

