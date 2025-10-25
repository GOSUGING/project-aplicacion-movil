// En AppTopBar.kt
package com.example.levelup.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.example.levelup.viewmodel.CartViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    cartViewModel: CartViewModel,
    onCartClick: () -> Unit
) {
    val itemCount = cartViewModel.cartItemCount.collectAsState().value

    TopAppBar(
        title = { Text("Level-Up Gamer") },
        actions = {
            // Usa un Badge para mostrar la cantidad de ítems
            BadgedBox(
                badge = {
                    if (itemCount > 0) {
                        Badge { Text("$itemCount") }
                    }
                }
            ) {
                IconButton(onClick = onCartClick) { // <-- 2. USA EL PARÁMETRO AQUÍ
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "Carrito de compras"
                    )
                }
            }
        }
    )
}
