package com.example.levelup.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.ExposedDropdownMenuDefaults.textFieldColors
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

// --- FIRMA CORREGIDA ---
// Ahora la función acepta PaddingValues, que le pasará el Scaffold desde MainActivity.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController,
    paddingValues: PaddingValues, // <-- 1. Parámetro añadido
    onLoginSuccess: (() -> Unit)? = null
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    fun isEmailValid(v: String) = Regex("\\S+@\\S+\\.\\S+").matches(v)

    Column(
        // 2. Se aplica el padding del Scaffold y se hace la columna scrollable.
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
            .background(Color.Black)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Iniciar sesión",
            style = MaterialTheme.typography.headlineSmall,
            color = Color.White
        )

        Spacer(Modifier.height(12.dp))

        if (error != null) {
            Text(text = error!!, color = Color(0xFFFF6B6B))
            Spacer(Modifier.height(8.dp))
        }

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo electrónico") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            // La función textFieldColors() se reutiliza de RegisterScreen.kt, la hemos copiado aquí.
            colors = textFieldColors()
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            colors = textFieldColors()
        )

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                error = null
                if (email.isBlank() || password.isBlank()) {
                    error = "Completa todos los campos"
                } else if (!isEmailValid(email)) {
                    error = "Correo electrónico inválido"
                } else {
                    // Aquí iría tu lógica real de login (API / Room / etc.)
                    // Por ahora, simplemente navega hacia atrás.
                    onLoginSuccess?.invoke() ?: navController.popBackStack()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF39FF14),
                contentColor = Color.Black
            )
        ) {
            Text("Entrar")
        }

        Spacer(Modifier.height(8.dp))

        TextButton(onClick = { navController.navigate("register") }) {
            Text("¿No tienes cuenta? Regístrate", color = Color(0xFF39FF14))
        }
    }
}
