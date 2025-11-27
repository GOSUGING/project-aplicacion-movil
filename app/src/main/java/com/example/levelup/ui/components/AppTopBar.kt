package com.example.levelup.ui.components

import android.media.MediaPlayer
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.levelup.R
import com.example.levelup.viewmodel.CartViewModel
import com.example.levelup.viewmodel.TopBarViewModel
import kotlin.random.Random

// =========================
// PALETA CYBERPUNK NEON
// =========================
private val NeonMagenta = Color(0xFFFF00FF)
private val NeonCyan = Color(0xFF00E5FF)
private val NeonLime = Color(0xFF39FF14)
private val NeonPurple = Color(0xFFB400FF)
private val NeonOrange = Color(0xFFFF6B00)
private val BgBlack = Color(0xFF050505)

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
    onAdminClick: () -> Unit,
    isAdmin: Boolean
) {
    val topBarVM: TopBarViewModel = hiltViewModel()
    val currentUser by topBarVM.currentUser.collectAsState()
    var menuOpen by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = BgBlack
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .padding(horizontal = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // =========================
            // MENÚ CYBERPUNK
            // =========================
            Box {
                IconButton(onClick = { menuOpen = true }) {
                    Icon(Icons.Default.Menu, tint = NeonCyan, contentDescription = "Menu")
                }

                DropdownMenu(
                    expanded = menuOpen,
                    onDismissRequest = { menuOpen = false },
                    modifier = Modifier.background(Color(0xFF0B0B0B))
                ) {
                    DropdownMenuItem(
                        text = { GlowText("Productos", NeonCyan) },
                        onClick = { menuOpen = false; onMenuProducts() }
                    )

                    DropdownMenuItem(
                        text = { GlowText("Categorías", NeonCyan) },
                        onClick = { menuOpen = false; onMenuCategories() }
                    )

                    Divider(color = NeonCyan.copy(alpha = 0.4f))

                    if (currentUser == null) {
                        DropdownMenuItem(
                            text = { RowWithIcon("Iniciar sesión", Icons.Default.Person, NeonMagenta) },
                            onClick = { menuOpen = false; onMenuLogin() }
                        )
                        DropdownMenuItem(
                            text = { RowWithIcon("Registrarse", Icons.Default.Add, NeonLime) },
                            onClick = { menuOpen = false; onMenuRegister() }
                        )
                    } else {
                        DropdownMenuItem(
                            text = { RowWithIcon("Mi Perfil", Icons.Default.AccountCircle, NeonLime) },
                            onClick = { menuOpen = false; onMenuProfile() }
                        )
                    }

                    if (isAdmin) {
                        Divider(color = NeonPurple.copy(alpha = 0.4f))

                        DropdownMenuItem(
                            text = { RowWithIcon("Panel Admin", Icons.Default.AdminPanelSettings, NeonOrange) },
                            onClick = { menuOpen = false; onAdminClick() }
                        )
                    }
                }
            }

            // =========================
            // TÍTULO HOLOGRÁFICO
            // =========================
            Text(
                "LEVEL-UP GAMER",
                modifier = Modifier
                    .weight(1f)
                    .clickable { onTitleClick() },
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                style = TextStyle(
                    color = NeonCyan,
                    shadow = Shadow(
                        color = NeonCyan.copy(alpha = 0.9f),
                        blurRadius = 24f
                    )
                )
            )

            // =========================
            // PERFIL
            // =========================
            IconButton(
                onClick = {
                    if (currentUser == null) onMenuLogin()
                    else onMenuProfile()
                }
            ) {
                Icon(Icons.Default.Person, tint = NeonMagenta, contentDescription = "Perfil")
            }

            // =========================
            // CARRITO CYBERPUNK
            // =========================
            CartIconWithParticles(cartViewModel, onClick = onCartClick)
        }
    }
}

@Composable
fun CartIconWithParticles(
    cartViewModel: CartViewModel,
    onClick: () -> Unit
) {
    val count by cartViewModel.cartItemCount.collectAsState(initial = 0)
    val ctx = LocalContext.current
    val beep = remember { MediaPlayer.create(ctx, R.raw.cart_add) }

    val scale = remember { Animatable(1f) }
    var particles by remember { mutableStateOf<List<Offset>>(emptyList()) }

    LaunchedEffect(count) {
        if (count > 0) {
            beep.start()

            scale.animateTo(1.35f, tween(120))
            scale.animateTo(1f, tween(150))

            particles = List(14) {
                Offset(
                    Random.nextFloat() * 60f - 30f,
                    Random.nextFloat() * -50f
                )
            }
        }
    }

    Box(
        modifier = Modifier
            .size(48.dp)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {

        Canvas(
            modifier = Modifier
                .matchParentSize()
                .alpha(0.9f)
        ) {
            particles.forEach { p ->
                drawCircle(
                    color = Color(
                        Random.nextInt(100, 255),
                        Random.nextInt(80, 255),
                        Random.nextInt(200, 255)
                    ),
                    radius = Random.nextInt(3, 9).toFloat(),
                    center = center + p
                )
            }
        }

        Icon(
            Icons.Default.ShoppingCart,
            contentDescription = "Carrito",
            tint = NeonLime,
            modifier = Modifier
                .size(32.dp)
                .graphicsLayer {
                    scaleX = scale.value
                    scaleY = scale.value
                }
        )

        if (count > 0) {
            Box(
                modifier = Modifier
                    .offset(x = 12.dp, y = (-12).dp)
                    .background(NeonMagenta, shape = CircleShape)
                    .padding(4.dp)
            ) {
                Text(
                    text = count.toString(),
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun GlowText(text: String, color: Color) {
    Text(
        text,
        color = color,
        fontSize = 18.sp,
        style = TextStyle(
            shadow = Shadow(color.copy(alpha = 0.8f), blurRadius = 18f)
        )
    )
}

@Composable
fun RowWithIcon(text: String, icon: ImageVector, tint: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, tint = tint, contentDescription = null)
        Spacer(Modifier.width(8.dp))
        GlowText(text, tint)
    }
}
