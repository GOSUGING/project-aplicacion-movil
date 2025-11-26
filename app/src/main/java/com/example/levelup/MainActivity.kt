package com.example.levelup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState // Importación necesaria
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.levelup.ui.components.AppTopBar
import com.example.levelup.ui.screens.*
import com.example.levelup.viewmodel.CartViewModel
import com.example.levelup.viewmodel.TopBarViewModel // Importación del ViewModel de la barra superior
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Se asume que LevelUpTheme está definido y se usa aquí
            // LevelUpTheme {
            AppNavigation()
            // }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // Obtenemos los ViewModels necesarios para la barra superior
    val cartViewModel: CartViewModel = hiltViewModel()
    val topBarViewModel: TopBarViewModel = hiltViewModel() // Nuevo ViewModel

    // Observamos el usuario actual
    val currentUser by topBarViewModel.currentUser.collectAsState()

    // Lógica para determinar si el usuario es administrador
    val isAdmin = currentUser?.role?.equals("ADMIN", ignoreCase = true) ?: false

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {

        // --- PANTALLAS PRINCIPALES CON LA BARRA DE NAVEGACIÓN ---

        val screensWithTopBar = listOf("home", "categories", "products", "profile", "login", "register", "admin")

        screensWithTopBar.forEach { screen ->
            composable(
                route = if (screen == "products") "products?category={category}" else screen,
                arguments = if (screen == "products") listOf(navArgument("category") {
                    type = NavType.StringType
                    nullable = true
                }) else emptyList()
            ) { backStackEntry ->

                Scaffold(
                    // CORRECCIÓN CLAVE: Aplica padding para empujar el contenido debajo de la barra de estado
                    modifier = Modifier.statusBarsPadding(),

                    topBar = {
                        AppTopBar(
                            cartViewModel = cartViewModel,
                            onCartClick = { navController.navigate("cart") },
                            onMenuProducts = { navController.navigate("products") },
                            onMenuCategories = { navController.navigate("categories") },
                            onMenuLogin = { navController.navigate("login") },
                            onMenuRegister = { navController.navigate("register") },
                            onMenuProfile = { navController.navigate("profile") },
                            onTitleClick = { navController.navigate("home") },

                            // --- PARÁMETROS NUEVOS AÑADIDOS ---
                            onAdminClick = { navController.navigate("admin") }, // La acción de navegación
                            isAdmin = isAdmin // El estado booleano observado
                            // ------------------------------------
                        )
                    },
                    containerColor = Color.Black
                ) { padding ->
                    // Decide qué pantalla mostrar basándose en la ruta
                    when (screen) {
                        "home" -> HomeScreen(
                            paddingValues = padding,
                            onNavigateToProducts = { navController.navigate("products") }
                        )
                        "categories" -> CategoriesScreen(
                            paddingValues = padding,
                            navController = navController
                        )
                        "products" -> {
                            val category = backStackEntry.arguments?.getString("category")
                            ProductsScreen(
                                paddingValues = padding,
                                navController = navController,
                                category = category
                            )
                        }
                        "profile" -> ProfileScreen(
                            paddingValues = padding,
                            navController = navController
                        )
                        "login" -> LoginScreen(
                            paddingValues = padding,
                            navController = navController,
                        )
                        "register" -> RegisterScreen(
                            paddingValues = padding,
                            navController = navController
                        )
                        "admin" -> AdminScreen(navController = navController)
                    }
                }
            }
        }

        // --- PANTALLAS CON DISEÑO PROPIO (SIN LA BARRA DE NAVEGACIÓN PRINCIPAL) ---

        composable("cart") {
            CartScreen(
                navController = navController,
                onCheckout = { navController.navigate("purchase") }
            )
        }

        composable("purchase") {
            PurchaseScreen(
                onPurchaseSuccess = { navController.navigate("purchase_result") }
            )
        }

        composable("purchase_result") {
            PurchaseResultScreen(
                onReturnHome = {
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                onViewBills = { /* Aquí iría la navegación a la pantalla de facturas */ }
            )
        }
    }
}