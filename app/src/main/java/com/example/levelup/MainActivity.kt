package com.example.levelup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.levelup.data.SessionManager
import com.example.levelup.ui.components.AppTopBar
import com.example.levelup.ui.screens.*
import com.example.levelup.ui.theme.LevelUpTheme
import com.example.levelup.viewmodel.CartViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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
            Scaffold(
                topBar = {
                    AppTopBar(
                        cartViewModel = cartViewModel,
                        onCartClick = { navController.navigate("cart") },
                        onMenuProducts = { navController.navigate("products") },
                        onMenuCategories = { navController.navigate("categories") },
                        onMenuLogin = { navController.navigate("login") },
                        onMenuRegister = { navController.navigate("register") },
                        onMenuProfile = { navController.navigate("profile") },
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
                        onMenuProfile = { navController.navigate("profile") },
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
                        onMenuProfile = { navController.navigate("profile") },
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

        composable("profile") {
            Scaffold(
                topBar = {
                    AppTopBar(
                        cartViewModel = cartViewModel,
                        onCartClick = { navController.navigate("cart") },
                        onMenuProducts = { navController.navigate("products") },
                        onMenuCategories = { navController.navigate("categories") },
                        onMenuLogin = { navController.navigate("login") },
                        onMenuRegister = { navController.navigate("register") },
                        onMenuProfile = { navController.navigate("profile") },
                        onTitleClick = { navController.navigate("home") }
                    )
                },
                containerColor = Color.Black
            ) { paddingValues ->
                ProfileScreen(
                    paddingValues = paddingValues,
                    navController = navController
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
                        onMenuProfile = { navController.navigate("profile") },
                        onTitleClick = { navController.navigate("home") }
                    )
                },
                containerColor = Color.Black
            ) { paddingValues ->
                LoginScreen(
                    paddingValues = paddingValues,
                    navController = navController,
                    onLoginSuccess = {
                        navController.navigate("profile") {
                            popUpTo("home")
                        }
                    }
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
                        onMenuProfile = { navController.navigate("profile") },
                        onTitleClick = { navController.navigate("home") }
                    )
                },
                containerColor = Color.Black
            ) { paddingValues ->
                RegisterScreen(paddingValues = paddingValues)
            }
        }

        composable("cart") {
            CartScreen(
                cartViewModel = cartViewModel,
                navController = navController
            )
        }


        composable("purchase") {
            PurchaseScreen(
                navController = navController,
                cartViewModel = cartViewModel
            )
        }
    }
}