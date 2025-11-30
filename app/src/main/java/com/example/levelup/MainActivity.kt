package com.example.levelup

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
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
import com.example.levelup.viewmodel.TopBarViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var onPermissionResult: ((Boolean) -> Unit)? = null

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            onPermissionResult?.invoke(granted)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CompositionLocalProvider(
                LocalRequestPermission provides { permission: String, callback: (Boolean) -> Unit ->
                    onPermissionResult = callback
                    requestPermissionLauncher.launch(permission)
                }
            ) {
                AppNavigation()
            }
        }
    }
}

val LocalRequestPermission =
    compositionLocalOf<(String, (Boolean) -> Unit) -> Unit> {
        error("Permission requester not initialized")
    }

@Composable
fun AppNavigation() {

    val navController = rememberNavController()
    val cartVM: CartViewModel = hiltViewModel()
    val topBarVM: TopBarViewModel = hiltViewModel()

    val currentUser by topBarVM.currentUser.collectAsState()
    val isAdmin = currentUser?.role?.uppercase()?.contains("ADMIN") == true

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {

        //-----------------------------------------------------------------------
        // SCREENS CON TOPBAR
        //-----------------------------------------------------------------------

        val screensWithTopBar = listOf(
            "home", "categories", "products",
            "profile", "admin_profile",
            "login", "register", "admin"
        )

        screensWithTopBar.forEach { screen ->

            composable(
                route = if (screen == "products") "products?category={category}" else screen,
                arguments =
                    if (screen == "products")
                        listOf(navArgument("category") {
                            type = NavType.StringType
                            nullable = true
                        })
                    else emptyList()
            ) { backStackEntry ->

                Scaffold(
                    modifier = Modifier.statusBarsPadding(),
                    topBar = {
                        AppTopBar(
                            cartViewModel = cartVM,
                            onCartClick = { navController.navigate("cart") },
                            onMenuProducts = { navController.navigate("products") },
                            onMenuCategories = { navController.navigate("categories") },
                            onMenuLogin = { navController.navigate("login") },
                            onMenuRegister = { navController.navigate("register") },

                            onMenuProfile = {
                                if (isAdmin) navController.navigate("admin_profile")
                                else navController.navigate("profile")
                            },

                            onTitleClick = { navController.navigate("home") }
                        )
                    },
                    containerColor = Color.Black
                ) { padding ->

                    when (screen) {

                        "home" -> HomeScreen(
                            paddingValues = padding
                        ) {
                            navController.navigate("products")
                        }

                        "categories" ->
                            CategoriesScreen(
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

                        "admin_profile" -> AdminProfileScreen(
                            paddingValues = padding,
                            navController = navController
                        )

                        "login" -> LoginScreen(
                            paddingValues = padding,
                            navController = navController
                        )

                        "register" -> RegisterScreen(
                            paddingValues = padding,
                            navController = navController
                        )

                        "admin" -> AdminProfileScreen(
                            paddingValues = padding,
                            navController = navController
                        )
                    }
                }
            }
        }

        //-----------------------------------------------------------------------
        // SCREENS SIN TOPBAR
        //-----------------------------------------------------------------------

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
                onBackHome = {
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                onViewBills = { navController.navigate("payments_history") }
            )
        }

        composable("payments_history") {
            PaymentsHistoryScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable("admin_users") {
            AdminUsersScreen(navController = navController)
        }

        composable("admin_products") {
            AdminProductsScreen(navController = navController)
        }



        composable(
            route = "productDetail/{id}",
            arguments = listOf(
                navArgument("id") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments!!.getLong("id")

            ProductDetailScreen(
                productId = id,
                navController = navController
            )
        }
    }
}
