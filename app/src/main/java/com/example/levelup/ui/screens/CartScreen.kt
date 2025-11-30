package com.example.levelup.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.levelup.model.CartItem
import com.example.levelup.viewmodel.CartViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.platform.testTag


// PALETA CYBERPUNK
private val NeonPink = Color(0xFFFF007F)
private val NeonBlue = Color(0xFF00E5FF)
private val DarkBg = Color(0xFF050508)
private val CardBg = Color(0xFF111118)

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
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF0A0A0A), Color(0xFF1A0033))
                )
            )
            .padding(16.dp)
    ) {

        Text(
            text = "CARRITO",
            style = TextStyle(
                color = NeonBlue,
                fontSize = 32.sp,
                shadow = Shadow(color = NeonBlue, blurRadius = 20f)
            ),
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { navController.navigate("home") },
            colors = ButtonDefaults.buttonColors(containerColor = NeonPink),
            modifier = Modifier
                .fillMaxWidth()
                .shadow(12.dp, RoundedCornerShape(12.dp))
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Volver",
                tint = Color.Black
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                "Volver al menú principal",
                color = Color.Black,
                style = TextStyle(fontSize = 16.sp)
            )
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
        modifier = Modifier
            .fillMaxSize()
            .testTag("cart_loading"),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = NeonBlue,
            strokeWidth = 4.dp
        )
    }
}

@Composable
fun ErrorCart(msg: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag("cart_error"),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = msg,
            color = NeonPink,
            style = TextStyle(
                shadow = Shadow(color = NeonPink, blurRadius = 20f)
            )
        )
    }
}

@Composable
fun EmptyCart() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag("cart_empty"),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Tu carrito está vacío",
            color = NeonBlue,
            style = TextStyle(
                shadow = Shadow(color = NeonBlue, blurRadius = 20f)
            )
        )
    }
}

@Composable
fun CartListContent(
    items: List<CartItem>,
    onDelete: (Long) -> Unit,
    onCheckout: () -> Unit
) {
    val subtotal = items.sumOf { it.price * it.qty }

    Column(modifier = Modifier.testTag("cart_list")) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
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
                .background(CardBg, RoundedCornerShape(12.dp))
                .border(2.dp, NeonPink, RoundedCornerShape(12.dp))
                .padding(16.dp)
        ) {
            Text(
                text = "Subtotal: $$subtotal",
                color = NeonBlue,
                style = TextStyle(
                    fontSize = 22.sp,
                    shadow = Shadow(color = NeonBlue, blurRadius = 20f)
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onCheckout,
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(14.dp, RoundedCornerShape(12.dp)),
                colors = ButtonDefaults.buttonColors(containerColor = NeonBlue)
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
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = CardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .border(2.dp, NeonBlue, RoundedCornerShape(14.dp)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = item.imageUrl,
                contentDescription = item.name,
                modifier = Modifier
                    .size(80.dp)
                    .padding(end = 12.dp)
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    item.name,
                    color = NeonPink,
                    style = TextStyle(
                        fontSize = 18.sp,
                        shadow = Shadow(color = NeonPink, blurRadius = 16f)
                    )
                )
                Text("Cantidad: ${item.qty}", color = Color.Gray)
                Text(
                    "$${item.price}",
                    color = NeonBlue,
                    style = TextStyle(
                        shadow = Shadow(color = NeonBlue, blurRadius = 16f)
                    )
                )
            }

            Button(
                onClick = onDelete,
                colors = ButtonDefaults.buttonColors(containerColor = NeonPink),
                modifier = Modifier
                    .shadow(8.dp, RoundedCornerShape(8.dp))
                    .testTag("delete_btn_${item.id}")
            ) {
                Text("X", color = Color.Black)
            }
        }
    }
}
