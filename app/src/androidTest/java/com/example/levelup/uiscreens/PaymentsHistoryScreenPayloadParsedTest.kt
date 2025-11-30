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

class PaymentsHistoryScreenPayloadParsedTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun renders_parsed_items_from_raw_payload() {
        val repo = mockk<PaymentRepository>()
        val raw = """[
            {"productId":1,"cantidad":2,"price":300},
            {"productId":2,"cantidad":1,"price":150}
        ]""".trimIndent()
        coEvery { repo.getPayments() } returns Result.success(
            listOf(
                PaymentDTO(1L, 3, 450L, "OK", "2024-01-01", "U", "Dir", 7L, raw)
            )
        )
        val vm = PaymentsHistoryViewModel(repo)

        compose.setContent {
            com.example.levelup.ui.screens.PaymentsHistoryScreen(viewModel = vm, onBack = {})
        }

        compose.onNodeWithText("• ID 1 - 2 un. - $300").assertIsDisplayed()
        compose.onNodeWithText("• ID 2 - 1 un. - $150").assertIsDisplayed()
    }
}

