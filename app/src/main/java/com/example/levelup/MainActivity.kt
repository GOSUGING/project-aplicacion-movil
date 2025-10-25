package com.example.levelup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.levelup.ui.components.GlobalScaffold
import com.example.levelup.ui.screens.*
import com.example.levelup.ui.theme.LevelUpTheme
import com.example.levelup.viewmodel.CartViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LevelUpTheme {
                val navController = rememberNavController()
                val cartViewModel: CartViewModel = viewModel()
                AppNavHost(navController = navController, cartViewModel = cartViewModel)
            }
        }
    }
}

@Composable
fun AppNavHost(
    navController: androidx.navigation.NavHostController,
    cartViewModel: CartViewModel
) {
    NavHost(navController = navController, startDestination = "home") {

        // ✅ Pantallas con TopBar global
        composable("home") {
            GlobalScaffold(navController, cartViewModel) {
                HomeScreen(
                    onNavigateToProducts = { navController.navigate("products") },
                    onNavigateToCart = { navController.navigate("cart") },
                    cartViewModel = cartViewModel
                )
            }
        }

        // 🔹 Categorías
        composable("categories") {
            GlobalScaffold(navController, cartViewModel) {
                CategoriesScreen(navController = navController)
            }
        }

        // 🔹 Products (sin filtro de categoría)
        composable("products") {
            GlobalScaffold(navController, cartViewModel) {
                ProductsScreen(
                    navController = navController,
                    cartViewModel = cartViewModel,
                    initialCategory = null
                )
            }
        }

        // 🔹 Products con categoría opcional: products?category=juegos
        composable(
            route = "products?category={category}",
            arguments = listOf(
                navArgument("category") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category")
            GlobalScaffold(navController, cartViewModel) {
                ProductsScreen(
                    navController = navController,
                    cartViewModel = cartViewModel,
                    initialCategory = category
                )
            }
        }

        // 🚫 Pantalla sin TopBar
        composable("cart") {
            CartScreen(cartViewModel = cartViewModel, navController = navController)
        }
    }
}
