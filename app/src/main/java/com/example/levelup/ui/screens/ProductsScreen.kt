package com.example.levelup.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.levelup.R
import com.example.levelup.model.Product
import com.example.levelup.viewmodel.CartViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProductsScreen(
    navController: NavController,
    cartViewModel: CartViewModel
) {
    val products = sampleProducts()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    // Navega a "home" y limpia la pila hasta home (opcional)
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = false }
                    }
                },
                content = {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Volver")
                }
            )
        },
        floatingActionButtonPosition = FabPosition.End
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Título o espacio superior opcional
            Text(
                text = "Productos",
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(12.dp)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxSize(),
                contentPadding = PaddingValues(bottom = 80.dp) // evitar que el FAB tape items
            ) {
                items(products) { p ->
                    Card(modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            if (p.imageUrl != null) {
                                AsyncImage(
                                    model = p.imageUrl,
                                    contentDescription = p.name,
                                    modifier = Modifier
                                        .height(120.dp)
                                        .fillMaxWidth()
                                )
                            } else if (p.imageRes != null) {
                                AsyncImage(
                                    model = p.imageRes,
                                    contentDescription = p.name,
                                    modifier = Modifier
                                        .height(120.dp)
                                        .fillMaxWidth()
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            Text(p.name, style = MaterialTheme.typography.subtitle1)
                            Text("$${p.price}", style = MaterialTheme.typography.body1)
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(onClick = { cartViewModel.addToCart(p) }, modifier = Modifier.fillMaxWidth()) {
                                Text("Agregar")
                            }
                        }
                    }
                }
            }
        }
    }
}

fun sampleProducts(): List<Product> {
    return listOf(
        Product(1, "Catán", "Juego de mesa", 29990, imageUrl = null, imageRes = R.drawable.catan),
        Product(2, "Carcassonne", "Juego", 24990, imageUrl = null, imageRes = R.drawable.carcassonne),
        // añade el resto de productos aquí...
    )
}
