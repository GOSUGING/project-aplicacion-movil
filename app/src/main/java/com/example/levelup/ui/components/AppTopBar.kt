package com.example.levelup.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.levelup.viewmodel.CartViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.collectAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    cartViewModel: CartViewModel,
    onCartClick: () -> Unit,
    onMenuClick: () -> Unit, // ⬅️ NUEVO parámetro para el botón hamburguesa
    backgroundColor: Color,
    contentColor: Color
) {
    TopAppBar(
        title = {
            Text(
                text = "Level-Up!",
                color = contentColor
            )
        },
        navigationIcon = {
            IconButton(onClick = onMenuClick) { // <-- Al hacer click, redirige a Productos
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menú",
                    tint = Color(0xFF39FF14)
                )
            }
        },
        actions = {
            // Ícono del carrito
            IconButton(onClick = onCartClick) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "Carrito",
                    tint = Color(0xFF39FF14)
                )
                if (cartViewModel.cartItems.collectAsState().value.isNotEmpty()) {
                    Badge(
                        containerColor = Color(0xFF39FF14),
                        contentColor = backgroundColor
                    ) {
                        Text(cartViewModel.cartItems.collectAsState().value.size.toString())
                    }
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = backgroundColor,   // Fondo negro
            titleContentColor = contentColor // Texto blanco
        ),
        scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    )
}
