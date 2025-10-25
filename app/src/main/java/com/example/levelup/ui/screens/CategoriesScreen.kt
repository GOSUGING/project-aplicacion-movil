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

data class CategoryUi(val id: String, val name: String)

@Composable
fun CategoriesScreen(navController: NavController) {
    val categories = listOf(
        CategoryUi("consolas", "Consolas"),
        CategoryUi("juegos", "Juegos"),
        CategoryUi("accesorios", "Accesorios"),
        CategoryUi("ropa", "Ropa Gamer"),
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Categorías",
            style = MaterialTheme.typography.headlineMedium,
            color = Color(0xFF39FF14),
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Adaptive(160.dp),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(categories, key = { it.id }) { cat ->
                Surface(
                    color = Color(0xFF111111),
                    contentColor = Color.White,
                    tonalElevation = 2.dp,
                    shadowElevation = 2.dp,
                    modifier = Modifier
                        .padding(8.dp)
                        .height(100.dp)
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

            // “Ver todos los productos”
            item {
                Surface(
                    color = Color.Transparent,
                    modifier = Modifier
                        .padding(8.dp)
                        .height(56.dp)
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
                            text = "Ver todos los productos",
                            style = MaterialTheme.typography.titleSmall,
                            color = Color(0xFF39FF14),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}
