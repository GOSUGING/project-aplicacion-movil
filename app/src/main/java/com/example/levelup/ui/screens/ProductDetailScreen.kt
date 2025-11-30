package com.example.levelup.ui.screens

import android.widget.Toast
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.levelup.viewmodel.CartViewModel
import com.example.levelup.viewmodel.ProductViewModel

// PALETA NEON
private val NeonGreen = Color(0xFF39FF14)
private val NeonCyan = Color(0xFF00E5FF)
private val NeonMagenta = Color(0xFFFF00FF)
private val DarkBg = Color(0xFF050505)

@Composable
fun ProductDetailScreen(
    productId: Long,
    navController: NavController,
    vm: ProductViewModel = hiltViewModel(),
    cartVM: CartViewModel = hiltViewModel()
) {
    val ui = vm.ui.collectAsState()
    val ctx = LocalContext.current

    LaunchedEffect(productId) { vm.loadProductById(productId) }

    val product = ui.value.selectedProduct
    val loading = ui.value.isLoading
    val error = ui.value.error

    // Animación glow suave
    val glow by animateFloatAsState(
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(1800),
            RepeatMode.Reverse
        ), label = ""
    )

    // Fondo degradado neon
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF020202),
                        Color(0xFF0A0A0A),
                        Color(0xFF040404)
                    )
                )
            )
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            //-------------------------------------------------------
            // LOADING / ERROR
            //-------------------------------------------------------
            if (loading) {
                Spacer(Modifier.height(30.dp))
                CircularProgressIndicator(color = NeonCyan)
                return@Column
            }

            if (error != null || product == null) {
                Spacer(Modifier.height(30.dp))
                Text("Error al cargar el producto", color = Color.Red)
                return@Column
            }

            //-------------------------------------------------------
            // IMAGEN DE PRODUCTO (GLASS CARD)
            //-------------------------------------------------------
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        20.dp,
                        spotColor = NeonCyan.copy(alpha = 0.5f),
                        ambientColor = NeonGreen.copy(alpha = 0.5f)
                    )
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color.White.copy(alpha = 0.04f),
                                Color.White.copy(alpha = 0.01f)
                            )
                        ),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .border(
                        2.dp,
                        Brush.horizontalGradient(
                            listOf(
                                NeonGreen.copy(alpha = glow),
                                NeonCyan,
                                NeonMagenta.copy(alpha = glow)
                            )
                        ),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .padding(14.dp)
            ) {
                AsyncImage(
                    model = product.img,
                    contentDescription = product.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                )
            }

            Spacer(Modifier.height(22.dp))

            //-------------------------------------------------------
            // NOMBRE DEL PRODUCTO (HOLOGRAMA)
            //-------------------------------------------------------
            Text(
                product.name,
                fontSize = 34.sp,
                fontWeight = FontWeight.Black,
                color = NeonGreen,
                style = androidx.compose.ui.text.TextStyle(
                    shadow = Shadow(
                        color = NeonGreen.copy(alpha = glow),
                        blurRadius = 40f
                    )
                )
            )

            Spacer(Modifier.height(8.dp))

            //-------------------------------------------------------
            // DESCRIPCIÓN GLASMORPHIC
            //-------------------------------------------------------
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Color.White.copy(alpha = 0.04f),
                        RoundedCornerShape(16.dp)
                    )
                    .padding(16.dp)
            ) {
                Text(
                    product.description ?: "",
                    fontSize = 18.sp,
                    color = Color(0xFFCEE0E0)
                )
            }

            Spacer(Modifier.height(16.dp))

            //-------------------------------------------------------
            // PRECIO CYBERPUNK
            //-------------------------------------------------------
            Text(
                "$${product.price}",
                fontSize = 30.sp,
                color = NeonMagenta,
                fontWeight = FontWeight.Bold,
                style = androidx.compose.ui.text.TextStyle(
                    shadow = Shadow(
                        color = NeonMagenta.copy(alpha = glow),
                        blurRadius = 30f
                    )
                )
            )

            Spacer(Modifier.height(20.dp))

            //-------------------------------------------------------
            // STOCK BADGE
            //-------------------------------------------------------
            Box(
                modifier = Modifier
                    .background(
                        if (product.stock > 0) NeonGreen else Color.Red,
                        CircleShape
                    )
                    .padding(horizontal = 18.dp, vertical = 10.dp)
            ) {
                Text(
                    "Stock: ${product.stock}",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.height(30.dp))

            //-------------------------------------------------------
            // BOTÓN AGREGAR AL CARRITO (PANEL TECNOLÓGICO)
            //-------------------------------------------------------
            Button(
                onClick = {
                    cartVM.addProduct(product, 1)
                    Toast.makeText(ctx, "Agregado al carrito", Toast.LENGTH_SHORT).show()
                },
                enabled = product.stock > 0,
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .shadow(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = NeonGreen,
                    disabledContainerColor = Color.DarkGray
                )
            ) {
                Text(
                    "AGREGAR AL CARRITO",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }

            Spacer(Modifier.height(45.dp))

            //-------------------------------------------------------
            // BOTÓN VOLVER (HOLOGRAMA FUTURISTA)
            //-------------------------------------------------------
            Text(
                text = "⬅ VOLVER A PRODUCTOS",
                color = NeonCyan,
                fontSize = 20.sp,
                modifier = Modifier
                    .clickable { navController.navigate("products") }
                    .padding(8.dp),
                style = androidx.compose.ui.text.TextStyle(
                    shadow = Shadow(
                        color = NeonCyan.copy(alpha = glow),
                        blurRadius = 30f
                    )
                )
            )
        }
    }
}
