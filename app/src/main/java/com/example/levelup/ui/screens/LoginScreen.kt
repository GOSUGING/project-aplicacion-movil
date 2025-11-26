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
import androidx.compose.ui.text.input.PasswordVisualTransformation // <-- Import necesario para ocultar contraseña
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.levelup.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onAdminLogin: () -> Unit,
    onNavigateToRegister: () -> Unit,
    paddingValues: PaddingValues
) {
    val vm: LoginViewModel = hiltViewModel()
    val ui by vm.ui.collectAsState()

    Column(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
            .background(Color.Black)
            .verticalScroll(rememberScrollState()) // Añadido para evitar que el teclado oculte elementos
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center // Centra el contenido verticalmente
    ) {
        Text(
            text = "Iniciar Sesión",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        )

        Spacer(Modifier.height(24.dp))

        // --- CAMPO DE EMAIL CORREGIDO ---
        OutlinedTextField(
            value = ui.email,
            onValueChange = { vm.onChange("email", it) },
            label = { Text("Correo Electrónico") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = appTextFieldColors() // <-- Se usa la función reutilizable
        )

        Spacer(Modifier.height(12.dp))

        // --- CAMPO DE CONTRASEÑA CORREGIDO ---
        OutlinedTextField(
            value = ui.password,
            onValueChange = { vm.onChange("password", it) },
            label = { Text("Contraseña") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(), // <-- Oculta el texto de la contraseña
            colors = appTextFieldColors() // <-- Se usa la función reutilizable
        )

        Spacer(Modifier.height(20.dp))

        // Botón de Login
        Button(
            onClick = {
                vm.login(
                    onSuccess = onLoginSuccess,
                    onAdmin = onAdminLogin
                )
            },
            enabled = !ui.isLoading, // Se deshabilita mientras carga
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
                Text("Ingresar")
            }
        }

        if (ui.error != null) {
            Spacer(Modifier.height(16.dp))
            Text(ui.error!!, color = MaterialTheme.colorScheme.error)
        }

        Spacer(Modifier.height(24.dp))

        // Botón para ir a Registro
        TextButton(onClick = onNavigateToRegister) {
            Text("¿No tienes cuenta? Regístrate", color = Color(0xFF39FF14))
        }
    }
}
