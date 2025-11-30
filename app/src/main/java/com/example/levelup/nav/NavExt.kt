package com.example.levelup.nav

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination

fun NavController.navigateAndClear(route: String) {
    navigate(route) {
        // elimina TODO lo previo en el stack (incluye el startDestination actual)
        popUpTo(graph.findStartDestination().id) { inclusive = true }
        launchSingleTop = true
        restoreState = false

    }

}
