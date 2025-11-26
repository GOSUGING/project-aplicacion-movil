package com.example.levelup.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
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
    // Observamos el estado del formulario desde el ViewModel
    val uiState = vm.uiState
    var successMessage by remember { mutableStateOf<String?>(null) }

    // Efecto para mostrar el mensaje de éxito y luego ocultarlo
    LaunchedEffect(uiState.successMessage) {
        if (uiState.successMessage != null) {
            successMessage = uiState.successMessage
            delay(3500)
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
        Text(
            "Mi perfil",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
        Text(
            "Actualiza tu información personal y preferencias.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )

        Spacer(Modifier.height(16.dp))

        if (successMessage != null) {
            Text(successMessage!!, color = Color(0xFF39FF14), style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(8.dp))
        }

        // --- Formulario de Perfil ---
        Column {
            // Nombre
            OutlinedTextField(
                value = uiState.name,
                onValueChange = { vm.onFieldChange("name", it) },
                label = { Text("Nombre completo") },
                modifier = Modifier.fillMaxWidth(),
                colors = appTextFieldColors(),
                singleLine = true
            )
            Spacer(Modifier.height(8.dp))

            // Email
            OutlinedTextField(
                value = uiState.email,
                onValueChange = { vm.onFieldChange("email", it) },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                colors = appTextFieldColors(),
                singleLine = true
            )
            Spacer(Modifier.height(8.dp))

            // Dirección (ejemplo)
            OutlinedTextField(
                value = uiState.address,
                onValueChange = { vm.onFieldChange("address", it) },
                label = { Text("Dirección (Opcional)") },
                modifier = Modifier.fillMaxWidth(),
                colors = appTextFieldColors(),
                singleLine = true
            )
            Spacer(Modifier.height(8.dp))

            // Teléfono (ejemplo)
            OutlinedTextField(
                value = uiState.phone,
                onValueChange = { vm.onFieldChange("phone", it) },
                label = { Text("Teléfono (Opcional)") },
                modifier = Modifier.fillMaxWidth(),
                colors = appTextFieldColors(),
                singleLine = true
            )

            Spacer(Modifier.height(24.dp))
            Text("Preferencias", style = MaterialTheme.typography.titleMedium, color = Color.White)

            // Checkboxes de Preferencias
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = uiState.newsletter,
                    onCheckedChange = { vm.onFieldChange("newsletter", it) },
                    colors = CheckboxDefaults.colors(checkedColor = Color(0xFF39FF14))
                )
                Text("Recibir newsletter", color = Color.White)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = uiState.promos,
                    onCheckedChange = { vm.onFieldChange("promos", it) },
                    colors = CheckboxDefaults.colors(checkedColor = Color(0xFF39FF14))
                )
                Text("Recibir promociones", color = Color.White)
            }

            Spacer(Modifier.height(24.dp))

            // Botones de acción
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { vm.saveProfile() },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF39FF14), contentColor = Color.Black)
                ) {
                    Text("Guardar cambios")
                }
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
