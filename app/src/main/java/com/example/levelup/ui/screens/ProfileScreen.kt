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
import coil.compose.AsyncImage // Carga y renderizado de imagenes (Coil)
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import android.Manifest
import android.net.Uri
import android.os.Build
import java.io.File
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

    // Detectar admin
    val isAdmin = uiState.role?.uppercase()?.contains("ADMIN") == true

    // Redirección solo una vez
    LaunchedEffect(isAdmin) {
        if (isAdmin) {
            navController.navigate("admin_profile") {
                popUpTo("profile") { inclusive = true }
            }
        }
    }

    if (isAdmin) return

    // Mensaje de éxito
    var successMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(uiState.successMessage) {
        uiState.successMessage?.let {
            successMessage = it
            delay(2500)
            successMessage = null
        }
    }

    // ============================
    // PERFIL NORMAL (USUARIO)
    // ============================
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
            .background(Color.Black)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {

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

        val context = LocalContext.current

        var showPicker by remember { mutableStateOf(false) }
        var cameraOutputUri by remember { mutableStateOf<Uri?>(null) }

        // Lanzador para capturar foto con camara
        val takePictureLauncher = rememberLauncherForActivityResult(
            ActivityResultContracts.TakePicture()
        ) { success ->
            if (success) {
                cameraOutputUri?.let { uri ->
                    vm.updateAvatar(uri.toString())
                    showPicker = false
                }
            }
        }

        // Lanzador para seleccionar imagen desde galeria
        val pickImageLauncher = rememberLauncherForActivityResult(
            ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            uri?.let {
                vm.updateAvatar(it.toString())
                showPicker = false
            }
        }

        // Solicitud de permiso de camara
        val requestCameraPermission = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted ->
            if (granted) {
                // Archivo temporal en cache para la foto
                val dir = File(context.cacheDir, "images").apply { mkdirs() }
                val file = File.createTempFile("profile_", ".jpg", dir)
                // Uri seguro via FileProvider para la camara
                val uri = FileProvider.getUriForFile(context, context.packageName + ".fileprovider", file)
                cameraOutputUri = uri
                takePictureLauncher.launch(uri)
            }
        }

        // Solicitud de permiso para leer imagenes (galeria)
        val requestGalleryPermission = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted ->
            if (granted) {
                pickImageLauncher.launch("image/*")
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            // Renderiza la foto de perfil actual (si existe)
            AsyncImage(
                model = uiState.avatar ?: "",
                contentDescription = null,
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.width(16.dp))

            OutlinedButton(onClick = { showPicker = true }) {
                Icon(Icons.Default.CameraAlt, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Editar foto de perfil")
            }
        }

        // Dialogo con las opciones de origen de la foto
        if (showPicker) {
            AlertDialog(
                onDismissRequest = { showPicker = false },
                confirmButton = {
                    Column {
                        Button(onClick = {
                            val permission = Manifest.permission.CAMERA
                            requestCameraPermission.launch(permission)
                        }) {
                            Icon(Icons.Default.CameraAlt, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text("Desde la cámara del teléfono")
                        }

                        Spacer(Modifier.height(8.dp))

                        Button(onClick = {
                            val permission = if (Build.VERSION.SDK_INT >= 33) {
                                Manifest.permission.READ_MEDIA_IMAGES
                            } else {
                                Manifest.permission.READ_EXTERNAL_STORAGE
                            }
                            requestGalleryPermission.launch(permission)
                        }) {
                            Icon(Icons.Default.PhotoLibrary, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text("Abrir galería")
                        }
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showPicker = false }) { Text("Cancelar") }
                },
                title = { Text("Cambiar foto de perfil") },
                text = { Text("Selecciona cómo deseas cambiar tu foto de perfil") }
            )
        }

        successMessage?.let {
            Text(it, color = Color(0xFF39FF14), fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
        }

        Column {

            OutlinedTextField(
                value = uiState.name,
                onValueChange = { vm.onFieldChange("name", it) },
                label = { Text("Nombre completo") },
                leadingIcon = { Icon(Icons.Default.Person, null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = uiState.email,
                onValueChange = { vm.onFieldChange("email", it) },
                label = { Text("Email") },
                leadingIcon = { Icon(Icons.Default.Email, null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = uiState.address,
                onValueChange = { vm.onFieldChange("address", it) },
                label = { Text("Dirección") },
                leadingIcon = { Icon(Icons.Default.LocationOn, null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = uiState.phone,
                onValueChange = { vm.onFieldChange("phone", it) },
                label = { Text("Teléfono") },
                leadingIcon = { Icon(Icons.Default.Phone, null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(Modifier.height(24.dp))

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

            // ==========================
            // BOTONES: GUARDAR + CERRAR SESIÓN
            // ==========================
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                // GUARDAR
                Button(
                    onClick = { vm.saveProfile() },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Save, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Guardar")
                }

                // CERRAR SESIÓN REAL
                OutlinedButton(
                    onClick = {
                        vm.logout()  // limpia token + user
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
