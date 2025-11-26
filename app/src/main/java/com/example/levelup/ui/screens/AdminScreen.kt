package com.example.levelup.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.levelup.viewmodel.AdminViewModel
import com.example.levelup.viewmodel.ProductViewModel

@Composable
fun AdminScreen(
    navController: NavController,
    viewModel: AdminViewModel = hiltViewModel()
) {
@Composable
fun AdminScreen(
    viewModel: ProductViewModel = hiltViewModel()
) {
    val ui by viewModel.ui.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadProducts()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Panel Administrativo",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(16.dp))

        when {
            ui.isLoading -> {
                CircularProgressIndicator(color = Color.White)
            }

            ui.error != null -> {
                Text(ui.error!!, color = Color.Red)
            }

            else -> {
                LazyColumn {
                    items(ui.products.size) { index ->
                        val p = ui.products[index]

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {

                                Text("Producto: ${p.name}")
                                Text("Stock actual: ${p.stock}")

                                Spacer(Modifier.height(8.dp))

                                Button(
                                    onClick = {
                                        // 🔥 SUMAR 5 STOCK
                                        viewModel.updateStock(
                                            id = p.id,
                                            amount = 5
                                        )
                                    }
                                ) {
                                    Text("Agregar +5 stock")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
