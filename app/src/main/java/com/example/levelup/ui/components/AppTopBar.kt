package com.example.levelup.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.IconButton
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.levelup.viewmodel.CartViewModel
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    cartViewModel: CartViewModel,
    onCartClick: () -> Unit,
    onMenuProducts: () -> Unit,
    onMenuCategories: () -> Unit,
    onMenuLogin: () -> Unit,
    onTitleClick: () -> Unit, // ⬅️ nuevo parámetro para navegar al home
    backgroundColor: Color = Color.Black,
    contentColor: Color = Color.White,
    title: String = "Level-Up!"
) {
    val neon = Color(0xFF39FF14)
    var menuOpen by remember { mutableStateOf(false) }

    val cartCount by cartViewModel.cartItems
        .map { list -> list.sumOf { it.quantity } }
        .collectAsState(initial = 0)

    TopAppBar(
        title = {
            Text(
                text = title,
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { onTitleClick() } // 🔥 al pulsar -> va al home
            )
        },
        navigationIcon = {
            IconButton(onClick = { menuOpen = true }) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menú",
                    tint = Color.White
                )
            }

            DropdownMenu(
                expanded = menuOpen,
                onDismissRequest = { menuOpen = false },
                containerColor = Color(0xFF111111)
            ) {
                DropdownMenuItem(
                    text = { Text("Productos", color = Color.White) },
                    onClick = {
                        menuOpen = false
                        onMenuProducts()
                    }
                )
                DropdownMenuItem(
                    text = { Text("Categorías", color = Color.White) },
                    onClick = {
                        menuOpen = false
                        onMenuCategories()
                    }
                )
                DropdownMenuItem(
                    text = {
                        RowWithIconText("Iniciar sesión", Icons.Default.Person)
                    },
                    onClick = {
                        menuOpen = false
                        onMenuLogin()
                    }
                )
            }
        },
        actions = {
            // Perfil directo
            IconButton(onClick = onMenuLogin) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Perfil",
                    tint = Color.White
                )
            }

            // Carrito con badge
            IconButton(onClick = onCartClick) {
                BadgedBox(
                    badge = {
                        if (cartCount > 0) {
                            Badge(
                                containerColor = neon,
                                contentColor = Color.Black
                            ) {
                                Text(cartCount.toString())
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "Carrito",
                        tint = Color.White
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = backgroundColor,
            titleContentColor = Color.White,
            navigationIconContentColor = Color.White,
            actionIconContentColor = Color.White
        ),
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
    )
}

@Composable
private fun RowWithIconText(text: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Row {
        Icon(icon, contentDescription = null, tint = Color(0xFF39FF14))
        Spacer(Modifier.width(6.dp))
        Text(text, color = Color.White)
    }
}
