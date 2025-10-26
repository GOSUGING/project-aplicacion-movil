// ProductsScreen.kt
package com.example.levelup.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.levelup.R
import com.example.levelup.model.Product
import com.example.levelup.viewmodel.CartViewModel

// --- FIRMA CORREGIDA Y SIMPLIFICADA ---
// La pantalla solo recibe lo que necesita. No tiene su propio Scaffold.
@Composable
fun ProductsScreen(
    paddingValues: PaddingValues, // Recibe el padding del GlobalScaffold
    navController: NavController,
    cartViewModel: CartViewModel,
    category: String? // La categoría viene directamente del NavHost
) {
    val allProducts = remember { sampleProducts() }
    val productsToShow = remember(category, allProducts) {
        if (category.isNullOrBlank()) {
            allProducts
        } else {
            allProducts.filter { it.category.equals(category, ignoreCase = true) }
        }
    }

    val title = if (category == null) "Todos los Productos" else category.replaceFirstChar { it.uppercase() }

    Column(
        // Aplica el padding del Scaffold aquí
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

        LazyVerticalGrid(
            columns = GridCells.Adaptive(160.dp),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
        ) {
            items(productsToShow, key = { it.id }) { product ->
                ProductCard(
                    product = product,
                    onAddToCart = { cartViewModel.addToCart(product) },
                    onClick = { /* Aquí podrías navegar a una pantalla de detalle del producto */ }
                )
            }
        }
    }
}

// --- WIDGET DE TARJETA DE PRODUCTO REUTILIZABLE ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProductCard(
    product: Product,
    onAddToCart: () -> Unit,
    onClick: () -> Unit
) {
    // Se usa Card de Material 3
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        onClick = onClick, // La tarjeta completa es clickeable
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            val imageModifier = Modifier
                .height(140.dp)
                .fillMaxWidth()

            if (product.imageRes != null) {
                Image(
                    painter = painterResource(id = product.imageRes),
                    contentDescription = product.name,
                    modifier = imageModifier,
                    contentScale = ContentScale.Crop
                )
            } else {
                AsyncImage(
                    model = product.imageUrl,
                    contentDescription = product.name,
                    modifier = imageModifier,
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.placeholder_image) // Placeholder
                )
            }

            Column(
                modifier = Modifier.padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "$${"%,.0f".format(product.price)}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFF39FF14)
                )
                Spacer(Modifier.height(8.dp))
                // Se usa Button de Material 3
                Button(
                    onClick = onAddToCart,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF39FF14),
                        contentColor = Color.Black
                    )
                ) {
                    Text("Agregar")
                }
            }
        }
    }
}

// --- DATOS DE EJEMPLO ---
// Añade 'category' y usa 'imageRes' para las imágenes locales
private fun sampleProducts(): List<Product> {
    return listOf(
        Product(1, "Juego de Mesa Catán", "Estrategia y comercio", 29990.0, imageUrl = null, imageRes = R.drawable.catan, category = "juegos"),
        Product(2, "Juego de Mesa Carcassonne", "Colocación de losetas", 24990.0, imageUrl = null, imageRes = R.drawable.carcassonne, category = "juegos"),
        Product(3, "PlayStation 5", "Consola de última generación", 549990.0, imageUrl = null, imageRes = R.drawable.ps5, category = "consolas"),
        Product(4, "Silla Gamer Ergonómica", "Comodidad para largas sesiones", 149990.0, imageUrl = null, imageRes = R.drawable.silla_gamer, category = "accesorios"),
        Product(5, "Audifonos Gamer HyperX", "Calidad de audio superior", 89990.0, imageUrl = null, imageRes = R.drawable.hyperx_cloud, category = "accesorios"),
        Product(6, "Mouse Logitech", "Precisión y respuesta rápida", 69990.0, imageUrl = null, imageRes = R.drawable.mouse_logitech, category = "accesorios"),
        Product(7, "Polera y Polerón Level Up", "Estilo y comodidad", 129990.0, imageUrl = null, imageRes = R.drawable.polera_gamer, category = "accesorios"),
        Product(8, "PC Gamer ASUS ROG", "Potencia y rendimiento", 1299990.0, imageUrl = null, imageRes = R.drawable.rogstrix, category = "consolas"),
        Product(9,"Control XBOX", "Precisión y respuesta rápida", 69990.0, imageUrl = null, imageRes = R.drawable.control_xbox, category = "accesorios"),
        Product(10, "Mousepad Razer", "Comodidad para el mouse", 29990.0, imageUrl = null, imageRes = R.drawable.mousepad_razer, category = "accesorios"),
        Product(11, "Teclado Gamer Razer", "Precisión y respuesta rápida", 69990.0, imageUrl = null, imageRes = R.drawable.razer_keyboard, category = "accesorios"),
        Product(12, "Notebook Victus Gamer", "Potencia y rendimiento", 800000.0, imageUrl = null, imageRes = R.drawable.victus, category = "consolas"),
        Product(13,"Audifonos Marshall Major IV", "Calidad para largas sesiones", 89990.0, imageUrl = null, imageRes = R.drawable.marshall_heardphones, category = "accesorios"),

        // Añade más productos para otras categorías
    )
}

