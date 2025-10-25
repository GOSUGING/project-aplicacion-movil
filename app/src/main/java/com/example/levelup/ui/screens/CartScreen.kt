package com.example.levelup.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Payment
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.levelup.viewmodel.CartViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.*

@Composable
fun CartScreen(
    cartViewModel: CartViewModel,
    navController: NavController? = null,
    onCheckout: (() -> Unit)? = null,
    onBack: (() -> Unit)? = null
) {
    val items by cartViewModel.cartItems.collectAsState()
    val total = cartViewModel.totalPrice()
    val nf = remember { NumberFormat.getCurrencyInstance(Locale("es", "CL")) }

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    var fabPressed by remember { mutableStateOf(false) }
    val fabScale by animateFloatAsState(if (fabPressed) 1.08f else 1f)

    Scaffold(
        scaffoldState = scaffoldState,
        backgroundColor = Color.Black,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                icon = { Icon(Icons.Default.Payment, contentDescription = "Pagar") },
                text = { Text("Pagar") },
                onClick = {
                    scope.launch {
                        fabPressed = true
                        delay(120)
                        fabPressed = false

                        val result = scaffoldState.snackbarHostState.showSnackbar(
                            message = "Pago realizado con éxito ✅",
                            actionLabel = "Ir a productos",
                            duration = SnackbarDuration.Short
                        )

                        if (result == SnackbarResult.ActionPerformed) {
                            navController?.navigate("products") {
                                popUpTo("home") { inclusive = false }
                                launchSingleTop = true
                            } ?: onCheckout?.invoke()
                        } else {
                            navController?.navigate("products") {
                                popUpTo("home") { inclusive = false }
                                launchSingleTop = true
                            } ?: onCheckout?.invoke()
                        }
                    }
                },
                modifier = Modifier
                    .padding(end = 16.dp, bottom = 8.dp)
                    .graphicsLayer(scaleX = fabScale, scaleY = fabScale),
                backgroundColor = Color(0xFF39FF14),
                contentColor = Color.Black
            )
        },
        floatingActionButtonPosition = FabPosition.End,
        bottomBar = {
            Surface(
                elevation = 8.dp,
                color = Color.Black,
                contentColor = Color.White
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = {
                        if (navController != null) {
                            val popped = navController.popBackStack("home", inclusive = false)
                            if (!popped) {
                                navController.navigate("home") { launchSingleTop = true }
                            }
                        } else {
                            onBack?.invoke()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver al inicio",
                            tint = Color(0xFF39FF14)
                        )
                    }

                    Text(
                        text = "Total: ${nf.format(total)}",
                        style = MaterialTheme.typography.h6,
                        color = Color.White,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            items(items) { cartItem ->
                val subtotal = cartItem.quantity * cartItem.product.price

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    // Nombre + subtotal
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(cartItem.product.name, color = Color.White)
                        Text(
                            nf.format(subtotal),
                            color = Color(0xFF39FF14)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Botones + cantidad + -
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = { cartViewModel.removeOne(cartItem.product.id) },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color.DarkGray,
                                contentColor = Color.White
                            ),
                            modifier = Modifier.size(36.dp)
                        ) {
                            Text("−")
                        }

                        Text(
                            text = "x${cartItem.quantity}",
                            color = Color.White,
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                        )

                        Button(
                            onClick = { cartViewModel.addToCart(cartItem.product) },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color(0xFF39FF14),
                                contentColor = Color.Black
                            ),
                            modifier = Modifier.size(36.dp)
                        ) {
                            Text("+")
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Button(
                            onClick = { cartViewModel.removeFromCart(cartItem.product.id) },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color(0xFF39FF14),
                                contentColor = Color.Black
                            )
                        ) {
                            Text("Eliminar")
                        }
                    }
                }

                Divider(color = Color(0x22FFFFFF))
            }

            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }
}
