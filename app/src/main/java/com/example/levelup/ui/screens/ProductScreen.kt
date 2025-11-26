package com.example.levelup.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.levelup.viewmodel.ProductViewModel
import com.example.levelup.model.Product

@Composable
fun ProductsScreen(
    onProductClick: (Long) -> Unit = {} // si quieres navegar al detalle después
) {
    val vm: ProductViewModel = hiltViewModel()
    val ui = vm.ui.collectAsState()

    // Cargar productos cuando se abre la pantalla
    LaunchedEffect(Unit) {
        vm.loadProducts()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0E0E0E)) // estilo gamer oscuro
            .padding(16.dp)
    ) {
        Text(
            text = "Productos",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            color = Color.White,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        when {
            ui.value.isLoading -> LoadingSection()
            ui.value.error != null -> ErrorSection(ui.value.error!!)
            else -> ProductList(
                products = ui.value.products,
                onProductClick = onProductClick
            )
        }
    }
}

@Composable
fun LoadingSection() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = Color.Cyan)
    }
}

@Composable
fun ErrorSection(message: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Error: $message", color = Color.Red)
    }
}

@Composable
fun ProductList(
    products: List<Product>,
    onProductClick: (Long) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(products) { product ->
            ProductCard(product = product, onClick = { onProductClick(product.id) })
        }
    }
}

@Composable
fun ProductCard(
    product: Product,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A))
    ) {

        Row(modifier = Modifier.padding(12.dp)) {

            // Imagen del producto
            AsyncImage(
                model = product.imageUrl,
                contentDescription = product.name,
                modifier = Modifier
                    .size(90.dp)
                    .padding(end = 12.dp),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = product.name,
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )

                Text(
                    text = product.description,
                    color = Color.Gray,
                    maxLines = 2,
                    modifier = Modifier.padding(vertical = 4.dp)
                )

                Text(
                    text = "$${product.price}",
                    color = Color.Cyan,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}
