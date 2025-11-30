package com.example.levelup.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.levelup.data.dto.ProductDTO
import com.example.levelup.viewmodel.AdminViewModel


@Composable
fun AdminProductsScreen(
    navController: NavController,
    vm: AdminViewModel = hiltViewModel()
) {
    val ui = vm.ui.collectAsState().value

    val neon = Color(0xFF39FF14)
    val bg = Color(0xFF0A0A0A)

    // BOX para soportar FAB flotante
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bg)
            .padding(16.dp)
    ) {

        Column(modifier = Modifier.fillMaxSize()) {

            // ========= TITULO =========
            Text(
                "GESTIÓN DE PRODUCTOS",
                color = neon,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(12.dp))

            // LOADING
            if (ui.isLoading) {
                CircularProgressIndicator(color = neon)
                return@Box
            }

            // ERROR
            ui.error?.let {
                Text(it, color = Color.Red)
            }

            LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                items(ui.products) { product ->

                    ProductAdminCard(
                        product = product,

                        // SUMAR STOCK
                        onIncrease = {
                            vm.editStock(
                                product = product,
                                newStock = product.stock + 1
                            )
                        },

                        // RESTAR STOCK
                        onDecrease = {
                            if (product.stock > 0) {
                                vm.editStock(
                                    product = product,
                                    newStock = product.stock - 1
                                )
                            }
                        },

                        // EDICIÓN FUTURA
                        onEdit = {
                            // pantalla futura de edición
                        }
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // ========= BOTÓN GRANDE (opcional) =========
            Button(
                onClick = { navController.popBackStack() },
                colors = ButtonDefaults.buttonColors(containerColor = neon),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.Black)
                Spacer(Modifier.width(6.dp))
                Text("Volver al Panel Admin", color = Color.Black)
            }
        }

        // ========= FAB FLOTANTE =========
        FloatingActionButton(
            onClick = { navController.popBackStack() },
            containerColor = Color(0xFF1F1F1F),
            contentColor = neon,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Volver"
            )
        }
    }
}


// ===============================================================
// CARD DE PRODUCTO PARA ADMIN
// ===============================================================
@Composable
fun ProductAdminCard(
    product: ProductDTO,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onEdit: () -> Unit
) {
    val neon = Color(0xFF39FF14)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, neon, RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF111111)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(Modifier.padding(16.dp)) {

            AsyncImage(
                model = "http://56.228.34.53:8085" + product.img,
                contentDescription = product.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .border(1.dp, neon),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.height(10.dp))

            Text(product.name, color = neon, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text("Categoría: ${product.category}", color = Color.Gray)
            Text("Precio: $${product.price}", color = Color.Gray)

            Spacer(Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {

                Text("Stock: ${product.stock}", color = Color.White, fontSize = 18.sp)

                Row(verticalAlignment = Alignment.CenterVertically) {

                    IconButton(onClick = onDecrease) {
                        Icon(Icons.Default.Remove, tint = Color.Red, contentDescription = "Restar")
                    }

                    Spacer(Modifier.width(6.dp))

                    IconButton(onClick = onIncrease) {
                        Icon(Icons.Default.Add, tint = neon, contentDescription = "Sumar")
                    }
                }

                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, tint = Color.Cyan, contentDescription = "Editar")
                }
            }
        }
    }
}
