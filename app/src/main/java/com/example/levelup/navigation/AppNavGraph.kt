package com.example.levelup.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.levelup.ui.screens.PurchaseScreen
import com.example.levelup.ui.screens.PurchaseResultScreen
import com.example.levelup.ui.screens.PaymentsHistoryScreen

@Composable
fun AppNavGraph(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = "payment"
    ) {

        composable("payment") {
            PurchaseScreen(
                onPurchaseSuccess = {
                    navController.navigate("purchase_result") {
                        popUpTo("payment") { inclusive = true }
                    }
                }
            )
        }

        composable(route = "purchase_result") {
            PurchaseResultScreen(
                onBackHome = {
                    navController.navigate("payment") {
                        popUpTo("payment") { inclusive = true }
                    }
                },
                onViewBills = {
                    navController.navigate("payments_history")
                }
            )
        }


        composable("paymentsHistory") {
            PaymentsHistoryScreen(
                onBack = { navController.popBackStack() }
            )
        }

    }
}