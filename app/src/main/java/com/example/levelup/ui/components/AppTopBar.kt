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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.levelup.data.session.UserSession
import com.example.levelup.ui.theme.Orbitron
import com.example.levelup.viewmodel.CartViewModel
import com.example.levelup.viewmodel.TopBarViewModel

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
    title: String = "Level-Up! Gamer"
) {
    val topBarViewModel: TopBarViewModel = hiltViewModel()
    val currentUser: UserSession? = topBarViewModel.currentUser.collectAsState().value


    val cartCount = cartViewModel.cartItemCount.collectAsState().value


    var menuOpen by remember { mutableStateOf(false) }
    val ripple = androidx.compose.material3.ripple()
    val interactionSource = remember { MutableInteractionSource() }
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

            // ---- MENU (3 BARRAS) ----
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

                    HorizontalDivider(color = Color(0x33FFFFFF))

                    if (currentUser == null) {
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
                        DropdownMenuItem(
                            text = { RowWithIconText("Mi Perfil", Icons.Default.AccountCircle) },
                            onClick = {
                                menuOpen = false
                                onMenuProfile()
                            }
                        )
                    }
                }
            }

            // ---- TÍTULO ----
            Text(
                text = title,
                color = Color.White,
                fontFamily = Orbitron,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = ripple
                    ) { onTitleClick() }
            )

            // ---- PERFIL ----
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

            // ---- CARRITO ----
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

@Composable
private fun RowWithIconText(text: String, icon: ImageVector) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = Color(0xFF39FF14))
        Spacer(Modifier.width(8.dp))
        Text(text, color = Color.White)
    }
}
