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
import androidx.compose.ui.Modifier
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

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    // estado para animar el FAB (scale)
    var fabPressed by remember { mutableStateOf(false) }
    val fabScale by animateFloatAsState(if (fabPressed) 1.08f else 1f)

    Scaffold(
        scaffoldState = scaffoldState,
        floatingActionButton = {
            // Extended FAB con animación de scale
            ExtendedFloatingActionButton(
                icon = { Icon(Icons.Default.Payment, contentDescription = "Pagar") },
                text = { Text("Pagar") },
                onClick = {
                    // animación breve + lógica de pago / snackbar
                    scope.launch {
                        // animación "pop"
                        fabPressed = true
                        delay(120) // breve pausa para la animación
                        fabPressed = false

                        // Mostrar snackbar y esperar resultado (ActionPerformed o Dismissed)
                        val result = scaffoldState.snackbarHostState.showSnackbar(
                            message = "Pago realizado con éxito ✅",
                            actionLabel = "Ir a productos",
                            duration = SnackbarDuration.Short
                        )

                        // Si hubo acción o no, navegamos a products (puedes cambiar lógica)
                        if (result == SnackbarResult.ActionPerformed) {
                            // acción del snackbar pulsada -> ir a products
                            if (navController != null) {
                                navController.navigate("products") {
                                    popUpTo("home") { inclusive = false }
                                }
                            } else {
                                onCheckout?.invoke()
                            }
                        } else {
                            // snackbar dismiss -> también navegamos a products (opcional)
                            if (navController != null) {
                                navController.navigate("products") {
                                    popUpTo("home") { inclusive = false }
                                }
                            } else {
                                onCheckout?.invoke()
                            }
                        }
                    }
                },
                // Aplicamos la escala animada al modifier
                modifier = Modifier
                    .padding(end = 16.dp, bottom = 8.dp)
                    .graphicsLayer(scaleX = fabScale, scaleY = fabScale)
            )
        },
        floatingActionButtonPosition = FabPosition.End,
        bottomBar = {
            Surface(elevation = 8.dp) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = {
                        if (navController != null) {
                            navController.navigate("products") {
                                popUpTo("home") { inclusive = false }
                            }
                        } else {
                            onBack?.invoke()
                        }
                    }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Volver a productos")
                    }

                    val nf = NumberFormat.getCurrencyInstance(Locale("es","CL"))
                    Text(
                        text = "Total: ${nf.format(total)}",
                        style = MaterialTheme.typography.h6,
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
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
                ) {
                    Text(
                        cartItem.product.name,
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp)
                    )

                    Text("x${cartItem.quantity}", modifier = Modifier.padding(end = 8.dp))

                    Button(onClick = { cartViewModel.removeOne(cartItem.product.id) }) {
                        Text("-")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = { cartViewModel.removeFromCart(cartItem.product.id) }) {
                        Text("Eliminar")
                    }
                }
                Divider()
            }

            item {
                Spacer(modifier = Modifier.height(80.dp)) // espacio para FAB/bottomBar
            }
        }
    }
}
