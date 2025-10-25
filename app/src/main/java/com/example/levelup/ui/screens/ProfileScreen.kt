package com.example.levelup.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.levelup.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(
    paddingValues: PaddingValues,
    navController: NavController,
    vm: ProfileViewModel = hiltViewModel()
) {
    val currentUser by vm.currentUser.collectAsState()

    Column(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (currentUser == null) {
            // Esto se muestra si el usuario cierra sesión y la pantalla sigue visible
            Text(
                "No has iniciado sesión.",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White
            )
            Spacer(Modifier.height(16.dp))
            Button(onClick = { navController.navigate("login") }) {
                Text("Ir a Iniciar Sesión")
            }
        } else {
            // Muestra la información del usuario
            Text(
                "Mi Perfil",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(24.dp))

            ProfileInfoRow(icon = Icons.Default.Person, text = currentUser!!.nombre)
            Spacer(Modifier.height(16.dp))
            ProfileInfoRow(icon = Icons.Default.Email, text = currentUser!!.email)
            Spacer(Modifier.height(32.dp))

            Button(
                onClick = {
                    vm.logout()
                    // Navega a home después de cerrar sesión
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB22222))
            ) {
                Icon(Icons.Default.ExitToApp, contentDescription = "Cerrar Sesión")
                Spacer(Modifier.width(8.dp))
                Text("Cerrar Sesión")
            }
        }
    }
}

@Composable
private fun ProfileInfoRow(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = Color(0xFF39FF14))
        Spacer(Modifier.width(16.dp))
        Text(text, style = MaterialTheme.typography.bodyLarge, color = Color.White)
    }
}
    