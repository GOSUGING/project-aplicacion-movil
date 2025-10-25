package com.example.levelup.ui.screens

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.levelup.R
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

// --- FIRMA CORREGIDA Y FINAL ---
// Ahora recibe PaddingValues en lugar de un Modifier.
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    paddingValues: PaddingValues, // <-- 1. Acepta PaddingValues
    onNavigateToProducts: () -> Unit
) {
    // El Scaffold se eliminó porque ya lo provee GlobalScaffold.
    LazyColumn(
        // 2. Aplica el padding del Scaffold PRIMERO
        modifier = Modifier
            .padding(paddingValues)
            .background(Color.Black)
            .fillMaxSize()
            // 3. Luego, aplica cualquier padding adicional para el contenido
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Título + subtítulo
        item {
            Text(
                text = "Bienvenidos a Level-Up Gamer",
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White
            )
            Text(
                text = "Tu destino definitivo para equipos y accesorios de juego.",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White
            )
            Spacer(Modifier.height(16.dp))
        }

        // Carrusel principal
        item {
            val heroPager = rememberPagerState(pageCount = { 3 })
            HorizontalPager(
                state = heroPager,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) { page ->
                val img = when (page) {
                    0 -> R.drawable.carousel_img_1
                    1 -> R.drawable.carousel_img_2
                    else -> R.drawable.carousel_img_3
                }
                // Carga segura de la imagen
                runCatching {
                    Image(
                        painter = painterResource(id = img),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }.onFailure {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Imagen no disponible", color = Color.White)
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = onNavigateToProducts, // Usa la acción que recibe
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF39FF14),
                    contentColor = Color.Black
                )
            ) { Text("Ver Productos", style = MaterialTheme.typography.labelLarge) }
            Spacer(Modifier.height(24.dp))
        }

        // Info
        item {
            InfoTripletSection()
            Spacer(Modifier.height(24.dp))
        }

        // Blogs
        item {
            Text(
                "Blogs Destacados 📰",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            BlogsSectionSafe()
            Spacer(Modifier.height(24.dp))
        }

        // Mapa
        item {
            Text(
                "Encuéntranos en el Mapa 📍",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            MapSectionSafe()
            Spacer(Modifier.height(24.dp))
        }

        // Footer
        item {
            FooterSection()
        }
    }
}


// --- EL RESTO DE LAS FUNCIONES PRIVADAS SE MANTIENEN EXACTAMENTE IGUAL ---
// No es necesario cambiar nada de aquí para abajo.

@Composable
private fun InfoTripletSection() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        SectionCard(
            title = "Quienes Somos 🎮",
            body = "Level-Up Gamer es una tienda online para gamers en Chile: consolas, accesorios, computadores y sillas, con despacho a todo el país."
        )
        SectionCard(
            title = "Misión 🚀",
            body = "Entregar productos de alta calidad con una experiencia de compra única y personalizada en todo Chile."
        )
        SectionCard(
            title = "Visión 🌟",
            body = "Ser la tienda online líder en productos gamer en Chile, reconocida por su innovación y servicio al cliente."
        )
    }
}

@Composable
private fun SectionCard(title: String, body: String) {
    Surface(
        color = Color(0xFF111111),
        contentColor = Color.White,
        tonalElevation = 2.dp,
        shadowElevation = 2.dp,
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.titleLarge, color = Color(0xFF39FF14))
            Spacer(Modifier.height(6.dp))
            Text(body, style = MaterialTheme.typography.bodyMedium, color = Color(0xFFD3D3D3))
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun BlogsSectionSafe() {
    val ctx = LocalContext.current
    val blogIds = remember {
        listOf("blog1", "blog2", "blog3").map { name ->
            ctx.safeDrawableId(name)
        }
    }

    val pager = rememberPagerState(pageCount = { blogIds.size })
    HorizontalPager(
        state = pager,
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
    ) { page ->
        val id = blogIds[page]
        Surface(
            color = Color(0xFF0F0F0F),
            shape = MaterialTheme.shapes.medium
        ) {
            if (id != null) {
                Image(
                    painter = painterResource(id = id),
                    contentDescription = "Blog #$page",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Imagen no disponible", color = Color.White)
                }
            }
        }
    }
}

private fun Context.safeDrawableId(name: String): Int? {
    val id = resources.getIdentifier(name, "drawable", packageName)
    return if (id == 0) null else id
}

@SuppressLint("UnrememberedMutableState")
@Composable
private fun MapSectionSafe() {
    val context = LocalContext.current
    val canShowMap = remember {
        GoogleApiAvailability.getInstance()
            .isGooglePlayServicesAvailable(context) == ConnectionResult.SUCCESS
    }

    if (!canShowMap) {
        Surface(
            color = Color(0xFF111111),
            shape = MaterialTheme.shapes.medium,
            tonalElevation = 2.dp,
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
        ) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Mapa no disponible en este dispositivo", color = Color.White)
            }
        }
        return
    }

    val valparaiso = LatLng(-33.0472, -71.6127)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(valparaiso, 12f)
    }
    Surface(
        color = Color(0xFF111111),
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 2.dp,
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
    ) {
        GoogleMap(
            cameraPositionState = cameraPositionState,
            modifier = Modifier.fillMaxSize()
        ) {
            Marker(
                state = MarkerState(position = valparaiso),
                title = "Level-Up Gamer (Valparaíso)",
                snippet = "¡Te esperamos!"
            )
        }
    }
}

@Composable
private fun FooterSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black)
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Aceptamos todo medio de pago",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = Color.White
        )
        Spacer(Modifier.height(10.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Chip("PayPal"); Chip("Visa"); Chip("Mastercard"); Chip("Apple Pay"); Chip("Google Pay")
        }
        Spacer(Modifier.height(10.dp))
        Text(
            "© 2025 Level-Up Gamer. Todos los derechos reservados.",
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFFD3D3D3)
        )
    }
}

@Composable
private fun Chip(label: String) {
    Surface(
        color = Color(0xFF39FF14),
        contentColor = Color.Black,
        shape = MaterialTheme.shapes.small,
        tonalElevation = 2.dp
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        )
    }
}
