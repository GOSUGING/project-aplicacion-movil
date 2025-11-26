package com.example.levelup.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.levelup.viewmodel.LoginViewModel

// --- FUNCIÓN DE UTILIDAD DE COLORES PRIVADA ---
// Se define como 'private' para evitar el error de "Conflicting overloads"
@Composable
private fun loginTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = Color.White,
    unfocusedTextColor = Color.White,
    cursorColor = Color(0xFF39FF14), // Verde Neón

    focusedBorderColor = Color(0xFF39FF14),
    unfocusedBorderColor = Color.Gray,

    focusedLabelColor = Color(0xFF39FF14),
    unfocusedLabelColor = Color.Gray,

    focusedContainerColor = Color.Transparent,
    unfocusedContainerColor = Color.Transparent,
)
// ---------------------------------------------------------------------------------

@Composable
fun LoginScreen(
    // La función se mantiene simple, solo necesita el NavController
    navController: NavController,
    paddingValues: PaddingValues
) {
    val vm: LoginViewModel = hiltViewModel()
    val ui by vm.ui.collectAsState()

    // No necesitamos definir onLoginSuccess/onAdminLogin aquí porque el ViewModel
    // nos dirá a dónde navegar mediante la callback onNavigate.

    // --- Definición de la acción de navegación a Registro (Sí se necesita) ---
    val onNavigateToRegister: () -> Unit = {
        navController.navigate("register")
    }
    // -------------------------------------------------------------------------

    Column(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
            .background(Color.Black)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Iniciar Sesión",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        )

        Spacer(Modifier.height(32.dp))

        // --- CAMPO DE EMAIL ---
        OutlinedTextField(
            value = ui.email,
            onValueChange = { vm.onChange("email", it) },
            label = { Text("Correo Electrónico", color = Color.White) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = loginTextFieldColors() // Uso de la función privada
        )

        Spacer(Modifier.height(16.dp))

        // --- CAMPO DE CONTRASEÑA ---
        OutlinedTextField(
            value = ui.password,
            onValueChange = { vm.onChange("password", it) },
            label = { Text("Contraseña", color = Color.White) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            colors = loginTextFieldColors() // Uso de la función privada
        )

        Spacer(Modifier.height(32.dp))

        // Botón de Login
        Button(
            onClick = {
                // CORRECCIÓN CLAVE: Llamada a vm.login con UNA SOLA función 'onNavigate'
                // que recibe la ruta (String) que el ViewModel devuelve.
                vm.login(
                    onNavigate = { route ->
                        navController.navigate(route) {
                            // Limpia la pila después de la navegación exitosa
                            popUpTo("home") { inclusive = true }
                        }
                    }
                )
            },
            enabled = !ui.isLoading,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF39FF14),
                contentColor = Color.Black
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            if (ui.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.Black,
                    strokeWidth = 2.dp
                )
            } else {
                Text("Ingresar", fontWeight = FontWeight.SemiBold)
            }
        }

        if (ui.error != null) {
            Spacer(Modifier.height(16.dp))
            Text(ui.error!!, color = MaterialTheme.colorScheme.error)
        }

        Spacer(Modifier.height(24.dp))

        // Botón para ir a Registro
        TextButton(onClick = onNavigateToRegister) {
            Text("¿No tienes cuenta? Regístrate", color = Color(0xFF39FF14), fontWeight = FontWeight.Medium)
        }
    }
}