package com.example.levelup.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
// --- ÍCONOS REQUERIDOS ---
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.* import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector // Necesario para RowWithIconText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.levelup.data.session.UserSession
import com.example.levelup.ui.theme.Orbitron
import com.example.levelup.viewmodel.CartViewModel
import com.example.levelup.viewmodel.TopBarViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

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
    // --- PARÁMETROS CONDICIONALES ---
    onAdminClick: () -> Unit,
    isAdmin: Boolean,
    // --------------------------------
    backgroundColor: Color = Color.Black,
    contentColor: Color = Color.White,
    title: String = "Level-Up! Gamer"
) {
    val topBarViewModel: TopBarViewModel = hiltViewModel()
    val currentUser by topBarViewModel.currentUser.collectAsState()
    val cartCount by cartViewModel.cartItemCount.collectAsState()
    var menuOpen by remember { mutableStateOf(false) }
    val neon = Color(0xFF39FF14)

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

            // ---- MENU (BOTÓN DE ÍCONO) ----
            Box {
                IconButton(onClick = { menuOpen = true }) {
                    Icon(
                        imageVector = Icons.Filled.Menu,
                        contentDescription = "Menú",
                        tint = Color.White
                    )
                }

                DropdownMenu(
                    expanded = menuOpen,
                    onDismissRequest = { menuOpen = false },
                    modifier = Modifier.background(Color(0xFF111111))
                ) {

                    // Ítems sin Icono
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

                    Divider(color = Color(0x33FFFFFF))

                    if (currentUser == null) {
                        // Login con Icono
                        DropdownMenuItem(
                            text = { RowWithIconText("Iniciar sesión", Icons.Default.Person) },
                            onClick = {
                                menuOpen = false
                                onMenuLogin()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Registrarse", color = Color.White) },
                            onClick = {
                                menuOpen = false
                                onMenuRegister()
                            }
                        )
                    } else {
                        // Perfil con Icono
                        DropdownMenuItem(
                            text = { RowWithIconText("Mi Perfil", Icons.Default.AccountCircle) },
                            onClick = {
                                menuOpen = false
                                onMenuProfile()
                            }
                        )
                    }

                    // --- OPCIÓN DE ADMIN CONDICIONAL ---
                    if (isAdmin) {
                        Divider(color = Color(0x33FFFFFF))
                        DropdownMenuItem(
                            text = { RowWithIconText("Admin Panel", Icons.Default.Menu, Color.Red) },
                            onClick = {
                                menuOpen = false
                                onAdminClick()
                            }
                        )
                    }
                }
            }

            // ---- TÍTULO ----
            Text(
                text = title,
                // ...
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp)
                    .clickable { onTitleClick() }
            )

            // --- BOTÓN CONDICIONAL ADMIN PAGE (ÍCONO) ---
            if (isAdmin) {
                IconButton(onClick = onAdminClick) {
                    Icon(
                        imageVector = Icons.Filled.Menu, // Usamos el icono de menú para representar Admin
                        contentDescription = "Panel Admin",
                        tint = Color.Red // Destacar el acceso admin
                    )
                }
            }
            // ------------------------------------------

            // ---- PERFIL (ICONO) ----
            IconButton(
                onClick = {
                    if (currentUser == null) onMenuLogin()
                    else onMenuProfile()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Perfil",
                    tint = Color.White
                )
            }

            // ---- CARRITO (ICONO CON BADGE) ----
            IconButton(onClick = onCartClick) {
                BadgedBox(
                    badge = {
                        if (cartCount > 0) {
                            Badge(
                                containerColor = neon,
                                contentColor = Color.Black
                            ) { Text(cartCount.toString()) }
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.ShoppingCart,
                        contentDescription = "Carrito",
                        tint = Color.White
                    )
                }
            }
        }
    }
}

// --- FUNCIÓN AUXILIAR MEJORADA PARA ÍCONOS Y TEXTO ---
@Composable
private fun RowWithIconText(text: String, icon: ImageVector, tint: Color = Color(0xFF39FF14)) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = tint)
        Spacer(Modifier.width(8.dp))
        Text(text, color = Color.White)
    }
}