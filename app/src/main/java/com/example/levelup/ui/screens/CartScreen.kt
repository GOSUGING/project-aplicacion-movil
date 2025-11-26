package com.example.levelup.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.levelup.model.CartItem
import com.example.levelup.viewmodel.CartViewModel

@Composable
fun CartScreen(
    onCheckout: () -> Unit = {},
    navController: NavHostController
) {
    val vm: CartViewModel = hiltViewModel()
    val ui = vm.ui.collectAsState()

    LaunchedEffect(Unit) {
        vm.loadCart()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D0D0D))
            .padding(16.dp)
    ) {

        Text(
            text = "Carrito",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { navController.navigate("home") },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF00BCD4),
                contentColor = Color.Black
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Volver",
                tint = Color.Black
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Volver al menú principal")
        }

        Spacer(modifier = Modifier.height(12.dp))

        when {
            ui.value.isLoading -> LoadingCart()

            ui.value.error != null -> ErrorCart(ui.value.error!!)

            ui.value.items.isEmpty() -> EmptyCart()

            else -> CartListContent(
                items = ui.value.items,
                onDelete = { vm.deleteItem(it) },
                onCheckout = onCheckout
            )
        }
    }
}

@Composable
fun LoadingCart() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = Color.Cyan)
    }
}

@Composable
fun ErrorCart(msg: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = msg, color = Color.Red)
    }
}

@Composable
fun EmptyCart() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Tu carrito está vacío", color = Color.Gray)
    }
}

@Composable
fun CartListContent(
    items: List<CartItem>,
    onDelete: (Long) -> Unit,
    onCheckout: () -> Unit
) {
    val subtotal = items.sumOf { it.price * it.qty }

    Column {
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(items) { item ->
                CartItemCard(
                    item = item,
                    onDelete = { onDelete(item.id) }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF141414))
                .padding(16.dp)
        ) {
            Text(
                text = "Subtotal: $$subtotal",
                color = Color.White,
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onCheckout,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00BCD4))
            ) {
                Text("Proceder al pago", color = Color.Black)
            }
        }
    }
}

@Composable
fun CartItemCard(
    item: CartItem,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A))
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = item.imageUrl,
                contentDescription = item.name,
                modifier = Modifier
                    .size(70.dp)
                    .padding(end = 12.dp)
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(item.name, color = Color.White)
                Text("Cantidad: ${item.qty}", color = Color.Gray)
                Text("$${item.price}", color = Color.Cyan)
            }

            Button(
                onClick = onDelete,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text("X", color = Color.White)
            }
        }
    }
}
