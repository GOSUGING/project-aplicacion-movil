package com.example.levelup.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

// Esta data class está bien, la mantenemos para estructurar los datos.
data class CategoryUi(val id: String, val name: String)

// --- FIRMA CORREGIDA ---
// Ahora acepta PaddingValues, que vendrán del GlobalScaffold.
@Composable
fun CategoriesScreen(
    navController: NavController,
    paddingValues: PaddingValues // <-- 1. Acepta PaddingValues
) {
    val categories = listOf(
        CategoryUi("consolas", "Consolas"),
        CategoryUi("juegos", "Juegos"),
        CategoryUi("accesorios", "Accesorios"),
        CategoryUi("ropa", "Ropa Gamer"),
        CategoryUi("graficas", "Tarjetas Graficas"),
    )

    Column(
        // 2. APLICA EL PADDING DEL SCAFFOLD AQUÍ
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
            .background(Color.Black),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Categorías",
            style = MaterialTheme.typography.headlineMedium,
            color = Color(0xFF39FF14),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Adaptive(160.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(categories, key = { it.id }) { cat ->
                Surface(
                    color = Color(0xFF111111),
                    contentColor = Color.White,
                    tonalElevation = 2.dp,
                    shadowElevation = 2.dp,
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier
                        .height(120.dp)
                        .fillMaxWidth()
                        .clickable {
                            // Navega a productos filtrando por categoría
                            navController.navigate("products?category=${cat.id}")
                        }
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = cat.name,
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White
                        )
                    }
                }
            }

            // "Ver todos los productos"
            item {
                Surface(
                    color = Color(0xFF39FF14), // Un color más llamativo para la acción
                    contentColor = Color.Black,
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier
                        .height(60.dp)
                        .fillMaxWidth()
                        .clickable {
                            // Sin categoría => muestra todo
                            navController.navigate("products")
                        }
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Ver todos",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}
