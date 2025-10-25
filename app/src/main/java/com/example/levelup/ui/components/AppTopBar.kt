package com.example.levelup.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Person
import com.example.levelup.viewmodel.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    cartViewModel: CartViewModel,
    onCartClick: () -> Unit,
    onMenuProducts: () -> Unit,
    onMenuCategories: () -> Unit,
    onMenuLogin: () -> Unit,
    backgroundColor: Color = Color.Black,
    contentColor: Color = Color.White,
    accentColor: Color = Color(0xFF39FF14) // verde neón
) {
    var menuOpen by remember { mutableStateOf(false) }
    val items = cartViewModel.cartItems.collectAsState()
    val totalQty = items.value.sumOf { it.quantity }

    TopAppBar(
        title = { Text("Level-Up!", color = contentColor) },
        navigationIcon = {
            // Botón hamburguesa
            IconButton(onClick = { menuOpen = true }) {
                Icon(imageVector = Icons.Filled.Menu, contentDescription = "Menú", tint = accentColor)
            }

            // Menú desplegable
            DropdownMenu(
                expanded = menuOpen,
                onDismissRequest = { menuOpen = false },
            ) ag{
                DropdownMenuItem(
                    text = { Text("Productos", color = contentColor) },
                    onClick = { menuOpen = false; onMenuProducts() },
                    leadingIcon = {
                        Icon(Icons.Filled.Storefront, contentDescription = null, tint = accentColor)
                    }
                )
                DropdownMenuItem(
                    text = { Text("Categorías", color = contentColor) },
                    onClick = { menuOpen = false; onMenuCategories() },
                    leadingIcon = {
                        Icon(Icons.Filled.Category, contentDescription = null, tint = accentColor)
                    }
                )
                Divider(thickness = 0.5.dp, color = Color(0x22FFFFFF))
                DropdownMenuItem(
                    text = { Text("Inicio de sesión", color = contentColor) },
                    onClick = { menuOpen = false; onMenuLogin() },
                    leadingIcon = {
                        Icon(Icons.Filled.Person, contentDescription = "Perfil", tint = accentColor)
                    }
                )
            }
        },
        actions = {
            // Carrito con badge
            IconButton(onClick = onCartClick) {
                if (totalQty > 0) {
                    BadgedBox(badge = {
                        Badge(
                            containerColor = accentColor,
                            contentColor = Color.Black
                        ) { Text(totalQty.toString()) }
                    }) {
                        Icon(
                            imageVector = Icons.Filled.ShoppingCart,
                            contentDescription = "Carrito",
                            tint = accentColor
                        )
                    }
                } else {
                    Icon(
                        imageVector = Icons.Filled.ShoppingCart,
                        contentDescription = "Carrito",
                        tint = accentColor
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = backgroundColor,
            titleContentColor = contentColor,
            navigationIconContentColor = contentColor,
            actionIconContentColor = contentColor
        )
    )
}
