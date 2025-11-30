package com.example.levelup.ui.screens

import android.content.Context
import androidx.compose.animation.core.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.levelup.R


// PALETA CYBERPUNK
private val NeonGreen = Color(0xFF39FF14)
private val NeonCyan = Color(0xFF00E5FF)
private val NeonMagenta = Color(0xFFFF00FF)
private val DarkBg = Color(0xFF050505)


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    paddingValues: PaddingValues,
    onNavigateToProducts: () -> Unit
) {
    // Glow animado del título
    val glow by animateFloatAsState(
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = ""
    )

    LazyColumn(
        modifier = Modifier
            .padding(paddingValues)
            .background(DarkBg)
            .fillMaxSize()
            .testTag("home_list")
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // ---------- TITULO HOLOGRAFICO ----------
        item {
            Spacer(Modifier.height(12.dp))

            Text(
                text = "LEVEL-UP GAMER",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = NeonGreen,
                style = TextStyle(
                    shadow = Shadow(
                        color = NeonGreen.copy(alpha = glow),
                        blurRadius = 26f
                    )
                )
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = "Equipamiento. Poder. Estilo.",
                fontSize = 18.sp,
                color = NeonCyan,
                fontWeight = FontWeight.Medium
            )

            Spacer(Modifier.height(16.dp))
        }

        // ---------- CAROUSEL CYBERPUNK ----------
        item {
            val heroPager = rememberPagerState(pageCount = { 3 })

            HorizontalPager(
                state = heroPager,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .border(
                        2.dp,
                        Brush.horizontalGradient(listOf(NeonCyan, NeonMagenta, NeonGreen)),
                        MaterialTheme.shapes.medium
                    )
                    .shadow(20.dp)
            ) { page ->

                val imageRes = when (page) {
                    0 -> R.drawable.carousel_img_1
                    1 -> R.drawable.carousel_img_2
                    else -> R.drawable.carousel_img_3
                }

                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(Modifier.height(20.dp))
        }

        // ---------- SECCIONES INFO CYBERPUNK ----------
        item {
            InfoTripletSection()
            Spacer(Modifier.height(24.dp))
        }

        // ---------- BLOGS CYBER ----------
        item {
            Text(
                "Blogs Destacados",
                style = MaterialTheme.typography.titleLarge,
                color = NeonCyan,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            BlogsSectionSafe()
            Spacer(Modifier.height(24.dp))
        }

        // ---------- FOOTER ----------
        item {
            FooterSection()
        }
    }
}

// -------------------------------------------------------
// SUBCOMPONENTES CYBERPUNK
// -------------------------------------------------------

@Composable
private fun InfoTripletSection() {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        SectionCard(
            title = "¿QUIÉNES SOMOS?",
            body = "Somos Level-Up Gamer: tecnología, energía y pasión gamer en un solo lugar.",
            color = NeonGreen
        )
        SectionCard(
            title = "MISIÓN",
            body = "Equiparte con productos de alto rendimiento para mejorar tu experiencia gaming.",
            color = NeonCyan
        )
        SectionCard(
            title = "VISIÓN",
            body = "Ser la comunidad gamer más influyente de Chile.",
            color = NeonMagenta
        )
    }
}

@Composable
private fun SectionCard(title: String, body: String, color: Color) {
    Surface(
        color = Color(0xFF0F0F0F),
        contentColor = Color.White,
        tonalElevation = 3.dp,
        shadowElevation = 3.dp,
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, color, MaterialTheme.shapes.medium)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(title, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = color)
            Spacer(Modifier.height(6.dp))
            Text(body, fontSize = 15.sp, color = Color(0xFFD3D3D3))
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun BlogsSectionSafe() {
    val ctx = LocalContext.current
    val blogIds = remember {
        listOf("blog1", "blog2", "blog3").map { name -> ctx.safeDrawableId(name) }
    }

    val pager = rememberPagerState(pageCount = { blogIds.size })

    HorizontalPager(
        state = pager,
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .border(
                2.dp,
                Brush.horizontalGradient(listOf(NeonMagenta, NeonCyan)),
                MaterialTheme.shapes.medium
            )
    ) { page ->

        val id = blogIds[page]

        Surface(
            color = Color(0xFF111111),
            shape = MaterialTheme.shapes.medium
        ) {
            if (id != null) {
                Image(
                    painter = painterResource(id),
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


// -------------------------------------------------------
// FOOTER CYBERPUNK
// -------------------------------------------------------
@Composable
private fun FooterSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(DarkBg)
            .padding(vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            "Aceptamos todo medio de pago",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = NeonGreen
        )

        Spacer(Modifier.height(12.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.horizontalScroll(rememberScrollState()).testTag("footer_chips")
        ) {
            Chip("PayPal")
            Chip("Visa")
            Chip("Mastercard")
            Chip("Apple Pay")
            Chip("Google Pay")
        }

        Spacer(Modifier.height(12.dp))

        Text(
            "© 2025 Level-Up Gamer",
            style = MaterialTheme.typography.bodySmall,
            color = NeonCyan
        )
    }
}

@Composable
private fun Chip(label: String) {
    Surface(
        color = NeonGreen,
        contentColor = Color.Black,
        shape = MaterialTheme.shapes.small,
        tonalElevation = 4.dp
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}
