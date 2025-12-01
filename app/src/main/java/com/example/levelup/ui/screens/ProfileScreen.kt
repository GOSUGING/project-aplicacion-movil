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
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import coil.compose.AsyncImage
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import android.net.Uri
import java.io.File
import androidx.core.content.FileProvider
import coil.request.ImageRequest
import coil.request.CachePolicy

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

        val context = androidx.compose.ui.platform.LocalContext.current
        var profileImageFile by remember { mutableStateOf<File?>(null) }
        var profileImageVersion by remember { mutableStateOf(0L) }

        LaunchedEffect(Unit) {
            val existing = File(context.filesDir, "profile_image.jpg")
            if (existing.exists()) profileImageFile = existing
        }

        val pickImageLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            uri?.let {
                val dest = File(context.filesDir, "profile_image.jpg")
                context.contentResolver.openInputStream(it)?.use { input ->
                    dest.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
                profileImageFile = dest
                profileImageVersion++
            }
        }

        val takePictureLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.TakePicture()
        ) { success: Boolean ->
            if (success) {
                val captured = File(context.cacheDir, "profile_capture.jpg")
                val dest = File(context.filesDir, "profile_image.jpg")
                if (captured.exists()) {
                    captured.inputStream().use { input ->
                        dest.outputStream().use { output ->
                            input.copyTo(output)
                        }
                    }
                    profileImageFile = dest
                    profileImageVersion++
                }
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF121212)),
            border = BorderStroke(1.dp, Color.DarkGray)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (profileImageFile != null) {
                    val request = ImageRequest.Builder(context)
                        .data(profileImageFile)
                        .diskCachePolicy(CachePolicy.DISABLED)
                        .memoryCachePolicy(CachePolicy.DISABLED)
                        .setParameter("version", profileImageVersion, memoryCacheKey = profileImageVersion.toString())
                        .build()
                    AsyncImage(
                        model = request,
                        contentDescription = "Foto de perfil",
                        modifier = Modifier
                            .size(96.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(96.dp)
                    )
                }

                Column(
                    modifier = Modifier.weight(1f),
                ) {
                    Text(
                        text = "Foto de perfil",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Agrega una imagen local que se guardará en el dispositivo.",
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(onClick = { pickImageLauncher.launch("image/*") }) {
                            Icon(Icons.Default.Image, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text("Galería")
                        }
                        OutlinedButton(onClick = {
                            val temp = File(context.cacheDir, "profile_capture.jpg")
                            if (!temp.exists()) temp.createNewFile()
                            val uri = FileProvider.getUriForFile(
                                context,
                                "${context.packageName}.fileprovider",
                                temp
                            )
                            takePictureLauncher.launch(uri)
                        }) {
                            Icon(Icons.Default.PhotoCamera, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text("Cámara")
                        }
                    }
                }
            }
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
