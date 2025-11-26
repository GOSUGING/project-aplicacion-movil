package com.example.levelup.ui.screens

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.levelup.viewmodel.ProductViewModel
import com.example.levelup.viewmodel.CartViewModel
import com.example.levelup.model.Product

// =================================================================
// 1. PRODUCTS SCREEN
// =================================================================

@Composable
fun ProductsScreen(
    onProductClick: (Long) -> Unit = {}
) {
    val vm: ProductViewModel = hiltViewModel()
    val vmCart: CartViewModel = hiltViewModel()

    val ui by vm.ui.collectAsState()
    var addedMsg by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) { vm.loadProducts() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0E0E0E))
            .padding(horizontal = 16.dp)
    ) {

        Text(
            text = "Productos",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            color = Color.White,
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        )

        if (addedMsg != null) {
            Text(
                text = addedMsg!!,
                color = Color(0xFF39FF14),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                textAlign = TextAlign.Center
            )
            LaunchedEffect(addedMsg) {
                kotlinx.coroutines.delay(3000)
                addedMsg = null
            }
        }

        when {
            ui.isLoading -> LoadingSection(Modifier.weight(1f))
            ui.error != null -> ErrorSection(ui.error!!, Modifier.weight(1f))
            else -> ProductList(
                products = ui.products,
                onProductClick = onProductClick,
                onAddToCart = { product ->
                    vmCart.addProduct(product, 1)  // 🔥 AHORA ENVÍAMOS EL PRODUCTO ENTERO
                    addedMsg = "✅ Producto agregado al carrito"
                },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

// =================================================================
// 2. PRODUCT LIST
// =================================================================

@Composable
fun ProductList(
    products: List<Product>,
    onProductClick: (Long) -> Unit,
    onAddToCart: (Product) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        contentPadding = PaddingValues(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        items(products, key = { it.id }) { product ->
            ProductCard1(
                product = product,
                onClick = { onProductClick(product.id) },
                onAddToCart = { onAddToCart(product) }
            )
        }
    }
}

// =================================================================
// 3. PRODUCT CARD — VERSIÓN FINAL Y ÚNICA
// =================================================================

@Composable
fun ProductCard1(
    product: Product,
    onAddToCart: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1A1A1A)
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {

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
                    println("🔥 BOTÓN AGREGAR FUNCIONANDO")
                    onAddToCart()
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF00BCD4)
                )
            ) {
                Text("Agregar al carrito", color = Color.Black)
            }
        }
    }
}

// =================================================================
// 4. ESTADOS
// =================================================================

@Composable
fun LoadingSection(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = Color(0xFF00BCD4))
    }
}

@Composable
fun ErrorSection(message: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 32.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "⚠️ Error al cargar productos: $message",
            color = Color.Red,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
