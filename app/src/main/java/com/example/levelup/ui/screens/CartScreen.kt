package com.example.levelup.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
// Imports de Material 3
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Payment
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.levelup.viewmodel.CartViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

// --- PANTALLA COMPLETAMENTE MIGRAda A MATERIAL 3 ---
@OptIn(ExperimentalMaterial3Api::class) // Necesario para algunas APIs de Material 3
@Composable
fun CartScreen(
    cartViewModel: CartViewModel,
    navController: NavController
) {
    val items by cartViewModel.cartItems.collectAsState()
    val total by remember(items) {
        derivedStateOf { cartViewModel.totalPrice() }
    }

    // 1. En Material 3, el estado del Snackbar se maneja con SnackbarHostState
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var fabPressed by remember { mutableStateOf(false) }
    val fabScale by animateFloatAsState(targetValue = if (fabPressed) 1.08f else 1f, label = "fabScaleAnimation")

    val clLocale = remember { Locale.Builder().setLanguage("es").setRegion("CL").build() }
    val nf = remember { NumberFormat.getCurrencyInstance(clLocale) }

    // 2. Usamos el Scaffold de Material 3
    Scaffold(
        containerColor = Color.Black,
        // El snackbar se define con un SnackbarHost
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                icon = { Icon(Icons.Default.Payment, contentDescription = "Pagar") },
                text = { Text("Pagar") },
                onClick = {
                    scope.launch {
                        fabPressed = true
                        delay(120)
                        fabPressed = false

                        val result = snackbarHostState.showSnackbar(
                            message = "Pago realizado con éxito ✅",
                            actionLabel = "Ir a productos",
                            duration = SnackbarDuration.Short
                        )

                        if (result == SnackbarResult.ActionPerformed) {
                            navController.navigate("products") {
                                popUpTo("home") { inclusive = false }
                                launchSingleTop = true
                            }
                        }
                    }
                },
                modifier = Modifier
                    .padding(end = 16.dp, bottom = 8.dp)
                    .graphicsLayer(scaleX = fabScale, scaleY = fabScale),
                // Los colores se definen de forma diferente en Material 3
                containerColor = Color(0xFF39FF14),
                contentColor = Color.Black
            )
        },
        floatingActionButtonPosition = FabPosition.End,
        bottomBar = {
            // 3. Surface y los demás componentes ahora son de Material 3
            Surface(
                tonalElevation = 8.dp,
                color = Color.Black,
                contentColor = Color.White
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color(0xFF39FF14)
                        )
                    }

                    Text(
                        text = "Total: ${nf.format(total)}",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
            }
        }
    ) { innerPadding ->
        if (items.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text("Tu carrito está vacío", style = MaterialTheme.typography.headlineSmall, color = Color.Gray)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                items(items, key = { it.product.id }) { cartItem ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            cartItem.product.name,
                            color = Color.White,
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 8.dp)
                        )

                        Text("x${cartItem.quantity}", color = Color.White, modifier = Modifier.padding(end = 12.dp))

                        // 4. Botones de Material 3, con colores actualizados
                        Button(
                            onClick = { cartViewModel.removeOne(cartItem.product.id) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.DarkGray,
                                contentColor = Color.White
                            ),
                            modifier = Modifier.size(40.dp),
                            contentPadding = PaddingValues(0.dp)
                        ) { Text("-") }

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            onClick = { cartViewModel.removeFromCart(cartItem.product.id) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFB22222), // Un rojo para "eliminar"
                                contentColor = Color.White
                            )
                        ) { Text("Quitar") }
                    }
                    HorizontalDivider(color = Color(0x33FFFFFF)) // Divider de Material 3
                }

                item { Spacer(modifier = Modifier.height(96.dp)) } // Espacio extra para el FAB
            }
        }
    }
}
