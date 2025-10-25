package com.example.levelup.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.levelup.viewmodel.CartViewModel
import com.example.levelup.viewmodel.TopBarViewModel // Importamos el nuevo ViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    cartViewModel: CartViewModel,
    onCartClick: () -> Unit,
    onMenuProducts: () -> Unit,
    onMenuCategories: () -> Unit,
    onMenuLogin: () -> Unit,
    onMenuRegister: () -> Unit,
    onMenuProfile: () -> Unit,
    onTitleClick: () -> Unit,
    backgroundColor: Color = Color.Black,
    contentColor: Color = Color.White,
    title: String = "Level-Up!"
) {
    // --- SECCIÓN CORREGIDA ---
    // 1. Se obtiene la instancia del TopBarViewModel usando hiltViewModel()
    val topBarViewModel: TopBarViewModel = hiltViewModel()
    // 2. Se observa el estado del usuario desde el nuevo ViewModel
    val currentUser by topBarViewModel.currentUser.collectAsState()
    //-------------------------

    val neon = Color(0xFF39FF14)
    var menuOpen by remember { mutableStateOf(false) }
    val cartCount by cartViewModel.cartItemCount.collectAsState()
    val interactionSource = remember { MutableInteractionSource() }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp),
        color = backgroundColor,
        contentColor = contentColor,
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // --- SECCIÓN DEL MENÚ (la lógica interna ya es correcta) ---
            Box {
                IconButton(onClick = { menuOpen = true }) {
                    Icon(
                        imageVector = Icons.Filled.Menu,
                        contentDescription = "Menú",
                        tint = contentColor
                    )
                }
                DropdownMenu(
                    expanded = menuOpen,
                    onDismissRequest = { menuOpen = false },
                    modifier = Modifier.background(Color(0xFF111111))
                ) {
                    DropdownMenuItem(
                        text = { Text("Productos", color = Color.White) },
                        onClick = { menuOpen = false; onMenuProducts() }
                    )
                    DropdownMenuItem(
                        text = { Text("Categorías", color = Color.White) },
                        onClick = { menuOpen = false; onMenuCategories() }
                    )
                    HorizontalDivider(color = Color(0x33FFFFFF))

                    if (currentUser == null) {
                        DropdownMenuItem(
                            text = { RowWithIconText("Iniciar sesión", Icons.Default.Person) },
                            onClick = { menuOpen = false; onMenuLogin() }
                        )
                        DropdownMenuItem(
                            text = { Text("Registrarse", color = Color.White) },
                            onClick = { menuOpen = false; onMenuRegister() }
                        )
                    } else {
                        DropdownMenuItem(
                            text = { RowWithIconText("Mi Perfil", Icons.Default.AccountCircle) },
                            onClick = { menuOpen = false; onMenuProfile() }
                        )
                    }
                }
            }

            // --- TÍTULO ---
            Text(
                text = title,
                color = contentColor,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = rememberRipple(bounded = false),
                        onClick = onTitleClick
                    )
            )

            // --- ACCIONES ---
            IconButton(onClick = {
                if (currentUser == null) {
                    onMenuLogin()
                } else {
                    onMenuProfile()
                }
            }) {
                Icon(Icons.Default.Person, contentDescription = "Perfil", tint = contentColor)
            }
            IconButton(onClick = onCartClick) {
                BadgedBox(
                    badge = {
                        if (cartCount > 0) {
                            Badge(containerColor = neon, contentColor = Color.Black) {
                                Text(cartCount.toString())
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.ShoppingCart,
                        contentDescription = "Carrito",
                        tint = contentColor
                    )
                }
            }
        }
    }
}

@Composable
private fun RowWithIconText(text: String, icon: ImageVector) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = Color(0xFF39FF14))
        Spacer(Modifier.width(8.dp))
        Text(text, color = Color.White)
    }
}
