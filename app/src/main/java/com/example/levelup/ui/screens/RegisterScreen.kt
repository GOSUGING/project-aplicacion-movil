package com.example.levelup.ui.screens

import android.app.DatePickerDialog
import androidx.compose.foundation.background
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.levelup.viewmodel.RegisterViewModel
import java.util.Calendar

@Composable
fun RegisterScreen(
    paddingValues: PaddingValues, // Parámetro añadido para recibir el padding del Scaffold
    vm: RegisterViewModel = viewModel()
) {
    val ui by vm.ui.collectAsState()

    Column(
        // Se aplica el padding del Scaffold y se hace la columna scrollable
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

        // Fecha nacimiento (DatePickerDialog)
        val ctx = LocalContext.current
        val cal = remember { Calendar.getInstance() }
        val dateDialog = remember {
            DatePickerDialog(
                ctx,
                { _, y, m, d ->
                    val mm = (m + 1).toString().padStart(2, '0')
                    val dd = d.toString().padStart(2, '0')
                    vm.onChange("fechaNacimiento", "$y-$mm-$dd")
                    vm.onBlur("fechaNacimiento")
                },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).apply {
                datePicker.maxDate = System.currentTimeMillis()
            }
        }

        OutlinedTextField(
            value = ui.fechaNacimiento,
            onValueChange = {}, // No se permite el cambio directo
            readOnly = true,    // Es solo de lectura
            label = { Text("Fecha de nacimiento (YYYY-MM-DD)") },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { dateDialog.show() }, // Muestra el diálogo al hacer clic
            colors = textFieldColors()
        )

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

    // Se usa Transparent para que tome el color del fondo de la Column
    focusedContainerColor = Color.Transparent,
    unfocusedContainerColor = Color.Transparent,
    disabledContainerColor = Color.Transparent,
    errorContainerColor = Color.Transparent
)
