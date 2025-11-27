package com.example.levelup.ui.screens

import android.Manifest
import android.app.DatePickerDialog
import android.location.Geocoder
import android.media.MediaPlayer
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.*
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.levelup.R
import com.example.levelup.viewmodel.RegisterViewModel
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.delay
import java.util.*

private val NeonCyan = Color(0xFF00E5FF)
private val NeonPurple = Color(0xFFFF00FF)
private val SoftBg = Color(0xFF0A0A0A)

@Composable
fun RegisterScreen(
    paddingValues: PaddingValues,
    navController: NavController,
    vm: RegisterViewModel = hiltViewModel()
) {
    val ctx = LocalContext.current
    val scope = rememberCoroutineScope()

    val beep = remember { MediaPlayer.create(ctx, R.raw.cart_add) }
    val fusedLocation = remember { LocationServices.getFusedLocationProviderClient(ctx) }

    val ui = vm.ui.collectAsState().value

    // =============== SNACKBAR ===============
    val snackbarHostState = remember { SnackbarHostState() }

    // Animación glow
    val glow by animateFloatAsState(
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(1500),
            RepeatMode.Reverse
        ),
        label = ""
    )

    // =============== PERMISO GPS ===============
    var gpsAllowed by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted -> gpsAllowed = granted }

    // =============== AUTO NAVEGAR AL LOGIN ===============
    LaunchedEffect(ui.success) {
        ui.success?.let { msg ->
            beep.start()
            snackbarHostState.showSnackbar(msg)
            delay(1500)
            navController.navigate("login") {
                popUpTo("register") { inclusive = true }
            }
        }
    }

    // =============== UI PRINCIPAL ===============
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
            .background(SoftBg)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // TÍTULO CYBERPUNK
        Text(
            "CREAR CUENTA",
            color = NeonCyan,
            style = MaterialTheme.typography.headlineLarge
        )
        Spacer(Modifier.height(16.dp))

        // ERRORES
        ui.error?.let {
            Text(it, color = Color(0xFFFF6B6B))
            Spacer(Modifier.height(8.dp))
        }

        // ===================== CAMPOS =====================

        NeoField(
            label = "Nombre completo",
            value = ui.nombre,
            onChange = { vm.onChange("nombre", it) }
        )
        Spacer(Modifier.height(12.dp))

        NeoField(
            label = "Correo electrónico",
            value = ui.email,
            onChange = { vm.onChange("email", it) }
        )
        Spacer(Modifier.height(12.dp))

        // FECHA +18
        BirthDateField(ui.fechaNacimiento) { vm.onChange("fechaNacimiento", it) }
        Spacer(Modifier.height(12.dp))

        // ========== TELÉFONO + SELECTOR PAÍS ==========

        val countries = listOf(
            "+56 Chile", "+57 Colombia", "+51 Perú",
            "+54 Argentina", "+52 México", "+1 USA", "+34 España"
        )

        var selectedCountry by remember { mutableStateOf(countries[0]) }

        Row(Modifier.fillMaxWidth()) {
            // Selector país
            Box(
                modifier = Modifier
                    .width(120.dp)
                    .border(
                        2.dp,
                        NeonPurple,
                        MaterialTheme.shapes.small
                    )
                    .clickable { }
                    .padding(12.dp)
            ) {
                var expanded by remember { mutableStateOf(false) }

                Text(selectedCountry, color = NeonCyan)

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    countries.forEach { c ->
                        DropdownMenuItem(
                            text = { Text(c) },
                            onClick = {
                                selectedCountry = c
                                vm.onChange("phone", c.split(" ")[0] + ui.phone)
                                expanded = false
                            }
                        )
                    }
                }

                Spacer(
                    Modifier
                        .matchParentSize()
                        .clickable { expanded = true }
                )
            }

            Spacer(Modifier.width(8.dp))

            NeoField(
                label = "Teléfono",
                value = ui.phone,
                onChange = { vm.onChange("phone", it) },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(Modifier.height(12.dp))

        // ========== DIRECCIÓN + GPS ==========
        NeoField(
            label = "Dirección",
            value = ui.address,
            onChange = { vm.onChange("address", it) }
        )
        Spacer(Modifier.height(8.dp))

        Button(
            onClick = {
                permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)

                if (gpsAllowed) {
                    fusedLocation.lastLocation.addOnSuccessListener { loc ->
                        if (loc != null) {
                            val geocoder = Geocoder(ctx, Locale.getDefault())
                            val addr = geocoder.getFromLocation(loc.latitude, loc.longitude, 1)

                            val text = if (!addr.isNullOrEmpty()) {
                                "${addr[0].thoroughfare ?: ""} ${addr[0].subThoroughfare ?: ""}, ${addr[0].locality}"
                            } else {
                                "Lat: ${loc.latitude}, Lon: ${loc.longitude}"
                            }

                            vm.onChange("address", text)
                            beep.start()
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = NeonPurple)
        ) {
            Text("Obtener Dirección GPS", color = Color.Black)
        }

        Spacer(Modifier.height(16.dp))

        // PASSWORDS
        PasswordNeo("Contraseña", ui.password) { vm.onChange("password", it) }
        Spacer(Modifier.height(12.dp))

        PasswordNeo("Confirmar contraseña", ui.password2) { vm.onChange("password2", it) }
        Spacer(Modifier.height(16.dp))

        // BOTÓN CREAR CUENTA
        Button(
            onClick = { vm.submit() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = NeonCyan)
        ) {
            Text("Crear Cuenta", color = Color.Black)
        }

        // SNACKBAR NEON
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.padding(8.dp)
        ) { data ->
            Snackbar(
                snackbarData = data,
                containerColor = NeonCyan,
                contentColor = Color.Black
            )
        }
    }
}

@Composable
fun NeoField(label: String, value: String, onChange: (String) -> Unit, modifier: Modifier = Modifier.fillMaxWidth()) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        modifier = modifier,
        label = { Text(label, color = NeonPurple) },
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = NeonCyan,
            unfocusedBorderColor = NeonPurple.copy(alpha = 0.4f),
            focusedLabelColor = NeonCyan,
            unfocusedLabelColor = NeonPurple.copy(alpha = 0.5f),
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            cursorColor = NeonCyan
        )
    )
}

@Composable
fun PasswordNeo(label: String, value: String, onChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(label, color = NeonPurple) },
        visualTransformation = PasswordVisualTransformation(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = NeonCyan,
            unfocusedBorderColor = NeonPurple.copy(alpha = 0.4f),
            focusedLabelColor = NeonCyan,
            unfocusedLabelColor = NeonPurple.copy(alpha = 0.5f),
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            cursorColor = NeonCyan
        )
    )
}

@Composable
fun BirthDateField(value: String, onSelect: (String) -> Unit) {
    val ctx = LocalContext.current

    val maxDate = Calendar.getInstance().apply { add(Calendar.YEAR, -18) }

    val dialog = DatePickerDialog(
        ctx,
        { _, y, m, d ->
            val month = (m + 1).toString().padStart(2, '0')
            val day = d.toString().padStart(2, '0')
            onSelect("$y-$month-$day")
        },
        maxDate.get(Calendar.YEAR),
        maxDate.get(Calendar.MONTH),
        maxDate.get(Calendar.DAY_OF_MONTH)
    ).apply {
        datePicker.maxDate = maxDate.timeInMillis
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, NeonCyan, MaterialTheme.shapes.small)
            .clickable { dialog.show() }
            .padding(16.dp)
    ) {
        Text(
            text = if (value.isBlank()) "Fecha de nacimiento (+18)" else value,
            color = if (value.isBlank()) NeonPurple.copy(alpha = 0.7f) else NeonCyan
        )
    }
}
