// Archivo: CartScreen.kt
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    cartViewModel: CartViewModel,
    navController: NavController
) {
    val items by cartViewModel.cartItems.collectAsState()
    val total by remember(items) {
        derivedStateOf { cartViewModel.totalPrice() }
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var fabPressed by remember { mutableStateOf(false) }
    val fabScale by animateFloatAsState(targetValue = if (fabPressed) 1.08f else 1f, label = "fabScaleAnimation")

    val clLocale = remember { Locale.Builder().setLanguage("es").setRegion("CL").build() }
    val nf = remember { NumberFormat.getCurrencyInstance(clLocale) }

    Scaffold(
        containerColor = Color.Black,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                icon = { Icon(Icons.Default.Payment, contentDescription = "Pagar") },
                text = { Text("Pagar") },
                onClick = {

                    if (items.isNotEmpty()) {
                        navController.navigate("purchase")
                    } else {
                        scope.launch {
                            snackbarHostState.showSnackbar("Tu carrito está vacío.")
                        }
                    }
                },
                modifier = Modifier
                    .padding(end = 16.dp, bottom = 8.dp)
                    .graphicsLayer(scaleX = fabScale, scaleY = fabScale),
                containerColor = Color.Gray,
                contentColor = Color.Black
            )
        },
        floatingActionButtonPosition = FabPosition.End,
        bottomBar = {
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
                            tint = Color.Black
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
                                containerColor = Color(0xFFB22222),
                                contentColor = Color.Black
                            )
                        ) { Text("Quitar") }
                    }
                    HorizontalDivider(color = Color(0x33FFFFFF))
                }

                item { Spacer(modifier = Modifier.height(96.dp)) }
            }
        }
    }
}
