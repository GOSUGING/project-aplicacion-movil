package com.example.levelup.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.levelup.viewmodel.ProfileViewModel
import kotlinx.coroutines.delay

@Composable
fun ProfileScreen(
    paddingValues: PaddingValues,
    navController: NavController,
    vm: ProfileViewModel = hiltViewModel()
) {
    val uiState = vm.uiState
    var successMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(uiState.successMessage) {
        if (uiState.successMessage != null) {
            successMessage = uiState.successMessage
            delay(3000)
            successMessage = null
        }
    }

    Column(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
            .background(Color.Black)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {

        // Títulos
        Text(
            "Mi Perfil",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )

        Text(
            "Administra tu información personal.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )

        Spacer(Modifier.height(16.dp))

        successMessage?.let {
            Text(it, color = Color(0xFF39FF14), fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
        }

        // -------------------------------
        // FORMULARIO CON ICONOS
        // -------------------------------
        Column {

            // NOMBRE
            OutlinedTextField(
                value = uiState.name,
                onValueChange = { vm.onFieldChange("name", it) },
                label = { Text("Nombre completo") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(Modifier.height(8.dp))

            // EMAIL
            OutlinedTextField(
                value = uiState.email,
                onValueChange = { vm.onFieldChange("email", it) },
                label = { Text("Email") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(Modifier.height(8.dp))

            // DIRECCIÓN
            OutlinedTextField(
                value = uiState.address,
                onValueChange = { vm.onFieldChange("address", it) },
                label = { Text("Dirección") },
                leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(Modifier.height(8.dp))

            // TELÉFONO
            OutlinedTextField(
                value = uiState.phone,
                onValueChange = { vm.onFieldChange("phone", it) },
                label = { Text("Teléfono") },
                leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(Modifier.height(24.dp))

            // -------------------------------
            // PREFERENCIAS CON ICONOS
            // -------------------------------
            Text("Preferencias", color = Color.White, fontWeight = FontWeight.SemiBold)

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = uiState.newsletter,
                    onCheckedChange = { vm.onFieldChange("newsletter", it) }
                )
                Icon(Icons.Default.Notifications, contentDescription = null, tint = Color.White)
                Spacer(Modifier.width(8.dp))
                Text("Recibir newsletter", color = Color.White)
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = uiState.promos,
                    onCheckedChange = { vm.onFieldChange("promos", it) }
                )
                Icon(Icons.Default.LocalOffer, contentDescription = null, tint = Color.White)
                Spacer(Modifier.width(8.dp))
                Text("Recibir promociones", color = Color.White)
            }

            Spacer(Modifier.height(24.dp))

            // -------------------------------
            // BOTONES CON ICONOS
            // -------------------------------
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                // Guardar
                Button(
                    onClick = { vm.saveProfile() },
                    modifier = Modifier.weight(1f),
                ) {
                    Icon(Icons.Default.Save, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Guardar")
                }

                // Cerrar sesión
                OutlinedButton(
                    onClick = {
                        vm.logout()
                        navController.navigate("home") {
                            popUpTo("home") { inclusive = true }
                        }
                    },
                    modifier = Modifier.weight(1f),
                    border = BorderStroke(1.dp, Color(0xFFFF6B6B))
                ) {
                    Icon(Icons.Default.ExitToApp, contentDescription = null, tint = Color(0xFFFF6B6B))
                    Spacer(Modifier.width(8.dp))
                    Text("Cerrar sesión", color = Color(0xFFFF6B6B))
                }
            }
        }
    }
}
