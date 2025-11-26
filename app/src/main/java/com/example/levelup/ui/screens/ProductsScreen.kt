package com.example.levelup.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.levelup.model.Product
import com.example.levelup.viewmodel.CartViewModel
import com.example.levelup.viewmodel.ProductViewModel

@Composable
fun ProductsScreen(
    paddingValues: PaddingValues,
    navController: NavController,
    category: String?
) {
    val vm: ProductViewModel = hiltViewModel()
    val cartVM: CartViewModel = hiltViewModel()
    val ui = vm.ui.collectAsState()

    LaunchedEffect(Unit) {
        vm.loadProducts()
    }

    val products = ui.value.products
    val productsToShow = remember(category, products) {
        if (category.isNullOrBlank()) products
        else products.filter { it.category.equals(category, ignoreCase = true) }
    }

    val title = category?.replaceFirstChar { it.uppercase() } ?: "Todos los Productos"

    Column(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
            .background(Color.Black),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White,
            modifier = Modifier.padding(16.dp)
        )

        when {
            ui.value.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.Cyan)
                }
            }

            ui.value.error != null -> {
                Text(
                    text = ui.value.error ?: "Error desconocido",
                    color = Color.Red
                )
            }

            else -> {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(160.dp),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    items(productsToShow, key = { it.id }) { product ->
                        ProductCard(
                            product = product,
                            onAddToCart = {
                                cartVM.addProduct(product, 1)   // 🔥 ENVÍA PRODUCTO COMPLETO
                            },
                            onClick = {
                                // navController.navigate("productDetail/${product.id}")
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProductCard(
    product: Product,
    onAddToCart: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {

            // 🔥 Solo IMAGEN clickeable para NO bloquear el botón
            AsyncImage(
                model = product.imageUrl,
                contentDescription = product.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clickable { onClick() }
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(product.name, color = Color.White)
            Text("$${product.price}", color = Color.Cyan)

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    println("🔥 BOTÓN AGREGAR FUNCIONA")
                    onAddToCart()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00BCD4))
            ) {
                Text("Agregar al carrito", color = Color.Black)
            }
        }
    }
}
