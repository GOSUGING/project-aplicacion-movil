package com.example.levelup.ui.components

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.compose.ui.graphics.Color
import com.example.levelup.viewmodel.CartViewModel

@Composable
fun GlobalScaffold(
    navController: NavController,
    cartViewModel: CartViewModel,
    content: @Composable (innerPadding: androidx.compose.foundation.layout.PaddingValues) -> Unit
) {
    Scaffold(
        topBar = {
            AppTopBar(
                cartViewModel = cartViewModel,
                onCartClick = { navController.navigate("cart") },
                onMenuProducts = { navController.navigate("products") },
                onMenuCategories = { navController.navigate("categories") },
                onMenuLogin = { /* próximamente */ },
                backgroundColor = Color.Black,
                onTitleClick = { navController.navigate("home") },
                contentColor = Color.White
            )
        },
        containerColor = Color.Black,
        content = content
    )
}
