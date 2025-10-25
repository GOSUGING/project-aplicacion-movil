package com.example.levelup.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.levelup.R
import com.example.levelup.ui.components.AppTopBar
import com.example.levelup.viewmodel.CartViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    onNavigateToProducts: () -> Unit,
    onNavigateToCart: () -> Unit,
    cartViewModel: CartViewModel
) {
    Scaffold(
        topBar = {
            AppTopBar(
                cartViewModel = cartViewModel,
                onCartClick = onNavigateToCart,
                onMenuProducts = onNavigateToProducts,
                onMenuCategories = { /* TODO */ },
                onMenuLogin = { /* TODO */ },
                onTitleClick = { onNavigateToProducts() },
                backgroundColor = Color.Black,
                contentColor = Color.White
            )
        },
        containerColor = Color.Black
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .background(Color.Black)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 🟢 Título + subtítulo
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

            // 🟢 Carrusel principal
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
                    Image(
                        painter = painterResource(id = img),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                // ⬇️ NUEVO ESPACIADO ENTRE EL CARRUSEL Y "QUIÉNES SOMOS"
                Spacer(modifier = Modifier.height(32.dp))
            }

            // 🟢 Info (Quiénes Somos / Misión / Visión)
            item {
                InfoTripletSection()
                Spacer(Modifier.height(24.dp))
            }

            // 🟢 Blogs
            item {
                Text(
                    "Blogs Destacados 📰",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                BlogsSection()
                Spacer(Modifier.height(24.dp))
            }

            // 🟢 Mapa
            item {
                Text(
                    "Encuéntranos en el Mapa 📍",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                MapSection()
                Spacer(Modifier.height(24.dp))
            }

            // 🟢 Footer
            item {
                FooterSection()
            }
        }
    }
}


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
private fun BlogsSection() {
    val blogPager = rememberPagerState(pageCount = { 3 })
    HorizontalPager(
        state = blogPager,
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
    ) { page ->
        val img = when (page) {
            0 -> R.drawable.blog1   // reemplaza por tus drawables
            1 -> R.drawable.blog2
            else -> R.drawable.blog3
        }
        Surface(
            color = Color(0xFF0F0F0F),
            shape = MaterialTheme.shapes.medium
        ) {
            Image(
                painter = painterResource(id = img),
                contentDescription = "Blog #$page",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
private fun MapSection() {
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
            Chip("PayPal")
            Chip("Visa")
            Chip("Mastercard")
            Chip("Apple Pay")
            Chip("Google Pay")
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
