package com.example.levelup.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.example.levelup.viewmodel.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    cartViewModel: CartViewModel,
    onCartClick: () -> Unit,
    onMenuProducts: () -> Unit,
    onMenuCategories: () -> Unit,
    onMenuLogin: () -> Unit,
    onMenuRegister: () -> Unit,
    onTitleClick: () -> Unit,
    backgroundColor: Color = Color.Black,
    contentColor: Color = Color.White,
    title: String = "Level-Up!"
) {
    val neon = Color(0xFF39FF14)
    var menuOpen by remember { mutableStateOf(false) }
    val cartCount by cartViewModel.cartItemCount.collectAsState()
    val interactionSource = remember { MutableInteractionSource() }

    // Usaremos un Surface y un Row para tener control total y evitar el crash
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp), // Altura estándar de una TopAppBar
        color = backgroundColor,
        contentColor = contentColor,
        shadowElevation = 4.dp // Sombra para darle profundidad
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // --- SECCIÓN DEL MENÚ (REEMPLAZO DEL navigationIcon) ---
            Box {
                IconButton(onClick = { menuOpen = true }) {
                    Icon(
                        imageVector = Icons.Filled.Menu,
                        contentDescription = "Menú",
                        tint = contentColor
                    )
                }
                // El DropdownMenu aquí, anclado al Box, ahora es estable.
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
                    DropdownMenuItem(
                        text = { RowWithIconText("Iniciar sesión", Icons.Filled.Person) },
                        onClick = { menuOpen = false; onMenuLogin() }
                    )
                    DropdownMenuItem(
                        text = { Text("Registrarse", color = Color.White) },
                        onClick = { menuOpen = false; onMenuRegister() }
                    )
                }
            }

            // --- TÍTULO ---
            Text(
                text = title,
                color = contentColor,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .weight(1f) // Ocupa el espacio restante
                    .padding(horizontal = 12.dp)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = rememberRipple(bounded = false),
                        onClick = onTitleClick
                    )
            )

            // --- ACCIONES (ICONOS DE LA DERECHA) ---
            IconButton(onClick = onMenuLogin) {
                Icon(Icons.Filled.Person, contentDescription = "Perfil", tint = contentColor)
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
