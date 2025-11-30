package com.example.levelup.uiscreens

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollToNode
import com.example.levelup.ui.screens.HomeScreen
import org.junit.Rule
import org.junit.Test

class HomeScreenInfoTripletBodiesVisibleTest {
    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun info_triplet_bodies_are_visible_after_scroll() {
        compose.setContent { HomeScreen(paddingValues = PaddingValues(), onNavigateToProducts = {}) }

        compose.onNodeWithTag("home_list").performScrollToNode(hasText("Somos Level-Up Gamer: tecnología, energía y pasión gamer en un solo lugar."))
        compose.onNodeWithText("Somos Level-Up Gamer: tecnología, energía y pasión gamer en un solo lugar.").assertIsDisplayed()

        compose.onNodeWithTag("home_list").performScrollToNode(hasText("Equiparte con productos de alto rendimiento para mejorar tu experiencia gaming."))
        compose.onNodeWithText("Equiparte con productos de alto rendimiento para mejorar tu experiencia gaming.").assertIsDisplayed()

        compose.onNodeWithTag("home_list").performScrollToNode(hasText("Ser la comunidad gamer más influyente de Chile."))
        compose.onNodeWithText("Ser la comunidad gamer más influyente de Chile.").assertIsDisplayed()
    }
}

