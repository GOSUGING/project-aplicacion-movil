package com.example.levelup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.levelup.ui.screens.CartScreen
import com.example.levelup.ui.screens.HomeScreen
import com.example.levelup.ui.screens.ProductsScreen
import com.example.levelup.ui.theme.LevelUpTheme
import com.example.levelup.viewmodel.CartViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LevelUpTheme {
                val navController = rememberNavController()
                val cartViewModel: CartViewModel = viewModel()

                // Esto está perfecto. La MainActivity solo debe configurar el entorno.
                AppNavHost(navController = navController, cartViewModel = cartViewModel)
            }
        }
    }
}

@Composable
fun AppNavHost(navController: androidx.navigation.NavHostController, cartViewModel: CartViewModel) {
    // La configuración del NavHost está correcta.
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                onNavigateToProducts = { navController.navigate("products") },
                onNavigateToCart = { navController.navigate("cart") }, // <-- AÑADE ESTA LÍNEA
                cartViewModel = cartViewModel
            )
        }
        composable("products") {
            // Pásale el navController a la pantalla de productos
            ProductsScreen(
                navController = navController,
                cartViewModel = cartViewModel
            )
        }
        composable("cart") {
            // PASAMOS navController aquí
            CartScreen(
                cartViewModel = cartViewModel,
                navController = navController
            )
        }
    }
}
