package com.example.levelup.ui.screens

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel // <-- 1. CAMBIA EL IMPORT
import androidx.navigation.NavController
import com.example.levelup.viewmodel.RegisterViewModel
import java.util.Calendar

@Composable
fun RegisterScreen(
    paddingValues: PaddingValues,
    vm: RegisterViewModel = hiltViewModel(),
    navController: NavController// <-- 2. USA hiltViewModel()
) {
    val ui by vm.ui.collectAsState()

    Column(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
            .background(Color.Black)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Formulario de Registro", color = Color.White, style = MaterialTheme.typography.headlineSmall)

        Spacer(Modifier.height(12.dp))

        if (ui.error != null) {
            Text(ui.error!!, color = Color(0xFFFF6B6B))
            Spacer(Modifier.height(8.dp))
        }
        if (ui.success != null) {
            Text(ui.success!!, color = Color(0xFF39FF14))
            Spacer(Modifier.height(8.dp))
        }

        // Nombre
        OutlinedTextField(
            value = ui.nombre,
            onValueChange = { vm.onChange("nombre", it) },
            label = { Text("Nombre completo") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = textFieldColors()
        )

        Spacer(Modifier.height(8.dp))

        // Email
        OutlinedTextField(
            value = ui.email,
            onValueChange = { vm.onChange("email", it) },
            label = { Text("Correo electrónico") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = textFieldColors()
        )

        Spacer(Modifier.height(8.dp))

        // --- SECCIÓN DE FECHA DE NACIMIENTO ---
        val context = LocalContext.current
        val maxDateCalendar = Calendar.getInstance().apply {
            add(Calendar.YEAR, -18)
        }
        val datePickerDialog = DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val monthFormatted = (month + 1).toString().padStart(2, '0')
                val dayFormatted = dayOfMonth.toString().padStart(2, '0')
                vm.onChange("fechaNacimiento", "$year-$monthFormatted-$dayFormatted")
                vm.onBlur("fechaNacimiento")
            },
            maxDateCalendar.get(Calendar.YEAR),
            maxDateCalendar.get(Calendar.MONTH),
            maxDateCalendar.get(Calendar.DAY_OF_MONTH)
        ).apply {
            datePicker.maxDate = maxDateCalendar.timeInMillis
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    shape = MaterialTheme.shapes.extraSmall
                )
                .clickable { datePickerDialog.show() }
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = if (ui.fechaNacimiento.isBlank()) "Fecha de nacimiento (Debes ser +18)" else ui.fechaNacimiento,
                color = if (ui.fechaNacimiento.isBlank()) Color(0xFFD3D3D3) else Color.White
            )
        }
        // --- FIN DE LA SECCIÓN DE FECHA ---

        Spacer(Modifier.height(8.dp))

        // Password
        OutlinedTextField(
            value = ui.password,
            onValueChange = { vm.onChange("password", it) },
            label = { Text("Contraseña") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            supportingText = { Text("Mín. 8 caráct., 1 número y 1 símbolo", color = Color(0xFFD3D3D3)) },
            colors = textFieldColors()
        )

        Spacer(Modifier.height(8.dp))

        // Confirmación
        OutlinedTextField(
            value = ui.password2,
            onValueChange = { vm.onChange("password2", it) },
            label = { Text("Confirmar contraseña") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            colors = textFieldColors()
        )

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = { vm.submit() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF39FF14),
                contentColor = Color.Black
            )
        ) {
            Text("Registrarse")
        }
    }
}

@Composable
private fun textFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = Color.White,
    unfocusedTextColor = Color.White,
    disabledTextColor = Color.White.copy(alpha = 0.5f),
    errorTextColor = Color.White,
    focusedBorderColor = Color(0xFF39FF14),
    unfocusedBorderColor = Color(0xFF333333),
    errorBorderColor = Color(0xFFFF6B6B),
    focusedLabelColor = Color(0xFF39FF14),
    unfocusedLabelColor = Color(0xFFD3D3D3),
    cursorColor = Color(0xFF39FF14),
    focusedContainerColor = Color.Transparent,
    unfocusedContainerColor = Color.Transparent,
    disabledContainerColor = Color.Transparent,
    errorContainerColor = Color.Transparent
)
