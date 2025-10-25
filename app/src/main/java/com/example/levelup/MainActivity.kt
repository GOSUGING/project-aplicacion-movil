package com.example.levelup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Scaffold // Importa el Scaffold de Material 3
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.levelup.ui.components.AppTopBar // Importamos AppTopBar directamente
import com.example.levelup.ui.screens.*
import com.example.levelup.ui.theme.LevelUpTheme
import com.example.levelup.viewmodel.CartViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LevelUpTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val cartViewModel: CartViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {

        // --- Pantallas que usan la barra de navegación ---

        composable("home") {
            // Se define el Scaffold directamente en la ruta
            Scaffold(
                topBar = {
                    AppTopBar(
                        cartViewModel = cartViewModel,
                        onCartClick = { navController.navigate("cart") },
                        onMenuProducts = { navController.navigate("products") },
                        onMenuCategories = { navController.navigate("categories") },
                        onMenuLogin = { navController.navigate("login") },
                        onMenuRegister = { navController.navigate("register") },
                        onTitleClick = { navController.navigate("home") }
                    )
                },
                containerColor = Color.Black
            ) { paddingValues ->
                HomeScreen(
                    paddingValues = paddingValues,
                    onNavigateToProducts = { navController.navigate("products") }
                )
            }
        }

        composable("categories") {
            Scaffold(
                topBar = {
                    AppTopBar(
                        cartViewModel = cartViewModel,
                        onCartClick = { navController.navigate("cart") },
                        onMenuProducts = { navController.navigate("products") },
                        onMenuCategories = { navController.navigate("categories") },
                        onMenuLogin = { navController.navigate("login") },
                        onMenuRegister = { navController.navigate("register") },
                        onTitleClick = { navController.navigate("home") }
                    )
                },
                containerColor = Color.Black
            ) { paddingValues ->
                CategoriesScreen(
                    paddingValues = paddingValues,
                    navController = navController
                )
            }
        }

        composable(
            route = "products?category={category}",
            arguments = listOf(navArgument("category") {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            })
        ) { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category")
            Scaffold(
                topBar = {
                    AppTopBar(
                        cartViewModel = cartViewModel,
                        onCartClick = { navController.navigate("cart") },
                        onMenuProducts = { navController.navigate("products") },
                        onMenuCategories = { navController.navigate("categories") },
                        onMenuLogin = { navController.navigate("login") },
                        onMenuRegister = { navController.navigate("register") },
                        onTitleClick = { navController.navigate("home") }
                    )
                },
                containerColor = Color.Black
            ) { paddingValues ->
                ProductsScreen(
                    paddingValues = paddingValues,
                    navController = navController,
                    cartViewModel = cartViewModel,
                    category = category
                )
            }
        }

        composable("login") {
            Scaffold(
                topBar = {
                    AppTopBar(
                        cartViewModel = cartViewModel,
                        onCartClick = { navController.navigate("cart") },
                        onMenuProducts = { navController.navigate("products") },
                        onMenuCategories = { navController.navigate("categories") },
                        onMenuLogin = { navController.navigate("login") },
                        onMenuRegister = { navController.navigate("register") },
                        onTitleClick = { navController.navigate("home") }
                    )
                },
                containerColor = Color.Black
            ) { paddingValues ->
                LoginScreen(
                    paddingValues = paddingValues,
                    navController = navController
                )
            }
        }

        composable("register") {
            Scaffold(
                topBar = {
                    AppTopBar(
                        cartViewModel = cartViewModel,
                        onCartClick = { navController.navigate("cart") },
                        onMenuProducts = { navController.navigate("products") },
                        onMenuCategories = { navController.navigate("categories") },
                        onMenuLogin = { navController.navigate("login") },
                        onMenuRegister = { navController.navigate("register") },
                        onTitleClick = { navController.navigate("home") }
                    )
                },
                containerColor = Color.Black
            ) { paddingValues ->
                RegisterScreen(paddingValues = paddingValues)
            }
        }

        // --- Pantalla que NO usa la barra de navegación ---

        composable("cart") {
            // CartScreen tiene su propio Scaffold y diseño, por lo que no se le añade uno aquí.
            CartScreen(
                cartViewModel = cartViewModel,
                navController = navController
            )
        }
    }
}
