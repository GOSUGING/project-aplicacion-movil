package com.example.levelup.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.levelup.viewmodel.ProfileViewModel

@Composable
fun AdminProfileScreen(
    paddingValues: PaddingValues,
    navController: NavController,
    vm: ProfileViewModel = androidx.hilt.navigation.compose.hiltViewModel()
) {
    val uiState = vm.uiState

    val neon = Color(0xFF39FF14)
    val neonCyan = Color(0xFF00E5FF)
    val neonRed = Color(0xFFFF0050)
    val bg = Color(0xFF050505)

    // Animación de glow
    val glow by animateFloatAsState(
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            tween(1500),
            RepeatMode.Reverse
        ),
        label = ""
    )

    Column(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
            .background(bg)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // ===== TITULO HOLOGRAFICO =====
        Text(
            "ADMIN PANEL",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = neon,
            style = TextStyle(
                shadow = Shadow(
                    neon.copy(alpha = glow),
                    blurRadius = 28f
                )
            )
        )

        Spacer(Modifier.height(20.dp))

        // ===== INFO DEL ADMIN =====
        AdminInfoBox(uiState.name, uiState.email)

        Spacer(Modifier.height(25.dp))

        // ===== OPCIONES DEL PANEL =====
        CyberCard("Gestión de Usuarios", neonCyan, Icons.Default.Person) {
            navController.navigate("admin_users")
        }

        Spacer(Modifier.height(14.dp))

        CyberCard("Gestión de Productos / Stock", neon, Icons.Default.Inventory) {
            navController.navigate("admin_products")
        }

        Spacer(Modifier.height(14.dp))

        CyberCard("Historial de Ventas", neonRed, Icons.Default.ReceiptLong) {
            navController.navigate("payments_history")
        }

        Spacer(Modifier.height(28.dp))

        // ===== CERRAR PANEL ADMIN → HOME REAL =====
        Button(
            onClick = {
                navController.navigate("home") {        // ← AHORA VA A HOME
                    popUpTo("admin_profile") { inclusive = true }
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = neonCyan),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.Black)
            Spacer(Modifier.width(8.dp))
            Text("Cerrar Panel Admin", color = Color.Black)
        }

        Spacer(Modifier.height(16.dp))

        // ===== CERRAR SESIÓN REAL =====
        OutlinedButton(
            onClick = {
                vm.logout()                      // ← borra token + usuario real
                navController.navigate("home") { // ← vuelve home
                    popUpTo(0) { inclusive = true }  // ← limpia TODO el backstack
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            border = BorderStroke(2.dp, neonRed)
        ) {
            Icon(Icons.Default.ExitToApp, contentDescription = null, tint = neonRed)
            Spacer(Modifier.width(8.dp))
            Text("Cerrar Sesión", color = neonRed)
        }
    }
}

@Composable
fun CyberCard(label: String, color: Color, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {

    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(12.dp),
                ambientColor = color
            ),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF111111))
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .border(2.dp, color, RoundedCornerShape(12.dp))
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(32.dp))
            Spacer(Modifier.width(16.dp))
            Text(label, fontSize = 19.sp, fontWeight = FontWeight.Bold, color = color)
        }
    }
}

@Composable
fun AdminInfoBox(name: String, email: String) {
    val neon = Color(0xFF39FF14)

    Surface(
        color = Color(0xFF0E0E0E),
        tonalElevation = 4.dp,
        shadowElevation = 6.dp,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp)) {
            Text("Administrador", color = neon, fontWeight = FontWeight.Bold)
            Text(name, color = Color.White)
            Spacer(Modifier.height(8.dp))
            Text("Correo", color = neon, fontWeight = FontWeight.Bold)
            Text(email, color = Color.White)
        }
    }
}
