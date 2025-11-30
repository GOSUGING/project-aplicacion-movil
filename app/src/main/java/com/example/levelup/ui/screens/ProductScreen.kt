package com.example.levelup.ui.screens

import android.media.MediaPlayer
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.levelup.R
import com.example.levelup.data.dto.ProductDTO
import com.example.levelup.viewmodel.CartViewModel
import com.example.levelup.viewmodel.ProductViewModel

// Paleta de colores cyberpunk
private val NeonGreen = Color(0xFF39FF14)
private val NeonCyan = Color(0xFF00E5FF)
private val NeonMagenta = Color(0xFFFF00FF)
private val DarkBg = Color(0xFF050505)

@Composable
fun ProductsScreen(
    paddingValues: PaddingValues,
    navController: NavController,
    category: String?
) {
    val vm: ProductViewModel = hiltViewModel()
    val cartVM: CartViewModel = hiltViewModel()
    val ui = vm.ui.collectAsState()

    LaunchedEffect(Unit) { vm.loadProducts() }

    val snackbarHostState = remember { SnackbarHostState() }
    var snackbarMessage by remember { mutableStateOf<String?>(null) }

    // MOSTRAR SNACKBAR CUANDO SE ACTIVA snackbarMessage
    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let {
            snackbarHostState.showSnackbar(it)
            snackbarMessage = null
        }
    }

    val products = ui.value.products

    val productsToShow = remember(category, products) {
        if (category.isNullOrBlank()) products
        else products.filter { it.category.equals(category, ignoreCase = true) }
    }

    val glow by animateFloatAsState(
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(1600), RepeatMode.Reverse
        ), label = ""
    )

    val title = category?.replaceFirstChar { it.uppercase() } ?: "Todos los Productos"

    Column(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
            .background(DarkBg),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = title,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = NeonGreen,
            style = TextStyle(
                shadow = Shadow(NeonGreen.copy(alpha = glow), blurRadius = 28f)
            ),
            modifier = Modifier.padding(16.dp)
        )

        when {
            ui.value.isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = NeonCyan)
                }
            }

            ui.value.error != null -> {
                Text(ui.value.error!!, color = Color.Red)
            }

            else -> {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(160.dp),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    items(productsToShow) { product ->

                        ProductCard(
                            product = product,
                            onAddToCart = {

                                cartVM.addProduct(product, 1)

                                // ACTIVA EL SNACKBAR DESDE COMPOSE
                                snackbarMessage = "${product.name} agregado 🎧"
                            },
                            onClick = {}
                        )
                    }
                }
            }
        }

        // SNACKBAR NEON
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.padding(bottom = 16.dp)
        ) { data ->
            Snackbar(
                snackbarData = data,
                containerColor = NeonCyan,
                contentColor = Color.Black
            )
        }
    }
}


// -------------------------------------------------------------
// PRODUCT CARD CYBERPUNK + ANIMACIÓN + SONIDO
// -------------------------------------------------------------
@Composable
fun ProductCard(
    product: ProductDTO,
    onAddToCart: () -> Unit,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    val mediaPlayer = remember { MediaPlayer.create(context, R.raw.cart_add) }

    // Animaciones
    val scale = remember { Animatable(1f) }
    val glow = remember { Animatable(0f) }

    // Este flag PERMITE activar la animación desde el onClick
    var animateNow by remember { mutableStateOf(false) }

    // Ejecutamos la animación solo cuando animateNow cambia a true
    LaunchedEffect(animateNow) {
        if (animateNow) {
            scale.animateTo(1.1f, tween(120))
            glow.animateTo(1f, tween(100))
            scale.animateTo(1f, tween(150))
            glow.animateTo(0f, tween(120))

            animateNow = false // reset
        }
    }

    val dynamicBorder = Brush.horizontalGradient(
        listOf(
            NeonCyan.copy(alpha = glow.value),
            NeonMagenta,
            NeonGreen.copy(alpha = glow.value)
        )
    )

    Card(
        modifier = Modifier
            .padding(8.dp)
            .graphicsLayer {
                scaleX = scale.value
                scaleY = scale.value
            }
            .shadow(
                elevation = 12.dp,
                ambientColor = NeonGreen,
                spotColor = NeonCyan
            )
            .border(2.dp, dynamicBorder, shape = MaterialTheme.shapes.medium)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color(0xFF111111))
    ) {

        Column(Modifier.padding(12.dp)) {

            AsyncImage(
                model = product.img,
                contentDescription = product.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
            )

            Spacer(Modifier.height(8.dp))

            Text(product.name, fontWeight = FontWeight.Bold, color = NeonGreen, fontSize = 18.sp)
            Text("$${product.price}", color = NeonCyan, fontSize = 16.sp)

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = {
                    onAddToCart()

                    // sonido
                    mediaPlayer.start()

                    // activar animación pop
                    animateNow = true
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = NeonGreen)
            ) {
                Text("Agregar al carrito", color = Color.Black)
            }
        }
    }
}

