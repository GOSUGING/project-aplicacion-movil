package com.example.levelup.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.levelup.viewmodel.LoginViewModel

// PALETA NEÓN CYBERPUNK
private val NeonGreen = Color(0xFF39FF14)
private val NeonPink = Color(0xFFFF008A)
private val CyberBlue = Color(0xFF00E5FF)
private val DarkBg = Color(0xFF020202)

@Composable
private fun loginTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = Color.White,
    unfocusedTextColor = Color.White,
    cursorColor = NeonGreen,

    focusedBorderColor = NeonGreen,
    unfocusedBorderColor = Color(0xFF444444),

    focusedLabelColor = NeonGreen,
    unfocusedLabelColor = Color.Gray,

    focusedContainerColor = Color.Transparent,
    unfocusedContainerColor = Color.Transparent,
)

@Composable
fun LoginScreen(
    navController: NavController,
    paddingValues: PaddingValues
) {
    val vm: LoginViewModel = hiltViewModel()
    val ui by vm.ui.collectAsState()

    val onNavigateToRegister = { navController.navigate("register") }

    Column(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF050010),
                        Color(0xFF0A001A),
                        Color(0xFF000000)
                    )
                )
            )
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // --- TÍTULO CYBERPUNK ---
        Text(
            text = "INICIAR SESIÓN",
            style = TextStyle(
                color = NeonGreen,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                shadow = Shadow(
                    color = NeonGreen,
                    blurRadius = 20f
                )
            )
        )

        Spacer(Modifier.height(32.dp))

        // --- EMAIL ---
        OutlinedTextField(
            value = ui.email,
            onValueChange = { vm.onChange("email", it) },
            label = { Text("Correo Electrónico", color = Color.White) },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 2.dp,
                    color = NeonGreen.copy(alpha = 0.6f),
                    shape = RoundedCornerShape(12.dp)
                ),
            colors = loginTextFieldColors()
        )

        Spacer(Modifier.height(20.dp))

        // --- PASSWORD ---
        OutlinedTextField(
            value = ui.password,
            onValueChange = { vm.onChange("password", it) },
            label = { Text("Contraseña", color = Color.White) },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    2.dp,
                    CyberBlue.copy(alpha = 0.7f),
                    RoundedCornerShape(12.dp)
                ),
            colors = loginTextFieldColors()
        )

        Spacer(Modifier.height(32.dp))

        // --- BOTÓN LOGIN CYBERPUNK ---
        Button(
            onClick = {
                vm.login(
                    onNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo("home") { inclusive = true }
                        }
                    }
                )
            },
            enabled = !ui.isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
                .shadow(18.dp, RoundedCornerShape(14.dp)),
            colors = ButtonDefaults.buttonColors(
                containerColor = NeonGreen,
                contentColor = Color.Black
            )
        ) {
            if (ui.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(26.dp),
                    color = Color.Black,
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    "INGRESAR",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // --- MENSAJE DE ERROR ---
        if (ui.error != null) {
            Spacer(Modifier.height(16.dp))
            Text(
                ui.error!!,
                color = NeonPink,
                style = TextStyle(
                    shadow = Shadow(NeonPink, blurRadius = 12f)
                )
            )
        }

        Spacer(Modifier.height(24.dp))

        // --- BOTÓN REGISTRO CYBER ---
        TextButton(onClick = onNavigateToRegister) {
            Text(
                "¿No tienes cuenta? Regístrate",
                color = CyberBlue,
                fontWeight = FontWeight.Medium,
                style = TextStyle(
                    shadow = Shadow(CyberBlue, blurRadius = 12f)
                )
            )
        }
    }
}
