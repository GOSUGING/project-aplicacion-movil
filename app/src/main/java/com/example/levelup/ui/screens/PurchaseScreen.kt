package com.example.levelup.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.levelup.viewmodel.CartViewModel
import com.example.levelup.viewmodel.CouponsViewModel
import com.example.levelup.viewmodel.PaymentViewModel

@Composable
fun PurchaseScreen(
    onPurchaseSuccess: () -> Unit = {} // Navegar a PurchaseResultScreen
) {
    val cartVM: CartViewModel = hiltViewModel()
    val couponVM: CouponsViewModel = hiltViewModel()
    val payVM: PaymentViewModel = hiltViewModel()

    val cartUi = cartVM.ui.collectAsState()
    val couponUi = couponVM.ui.collectAsState()
    val payUi = payVM.ui.collectAsState()

    var couponCode by remember { mutableStateOf("") }

    // Cargar carrito cuando se abre
    LaunchedEffect(Unit) {
        cartVM.loadCart()
    }

    // Calcular subtotal
    val subtotal = cartUi.value.items.sumOf { it.price * it.quantity }

    // Calcular descuento si existe cupón válido
    val discount = couponUi.value.coupon?.discount ?: 0
    val total = subtotal - (subtotal * discount / 100)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D0D0D))
            .padding(16.dp)
    ) {
        Text(
            text = "Confirmar Compra",
            color = Color.White,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        // SUBTOTAL
        SummaryRow("Subtotal", subtotal)

        // CUPÓN
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = couponCode,
            onValueChange = { couponCode = it },
            placeholder = { Text("Código de cupón") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFF1A1A1A),
                unfocusedContainerColor = Color(0xFF1A1A1A),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedPlaceholderColor = Color.Gray,
                unfocusedPlaceholderColor = Color.Gray
            )
        )


        Button(
            onClick = { couponVM.fetchCoupon(couponCode) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00BCD4))
        ) {
            Text("Aplicar cupón")
        }

        if (couponUi.value.error != null) {
            Text(
                text = couponUi.value.error!!,
                color = Color.Red,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        // DESCUENTO
        if (discount > 0) {
            SummaryRow("Descuento (${discount}%)", -(subtotal * discount / 100))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // TOTAL FINAL
        SummaryRow("Total Final", total, highlight = true)

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                payVM.checkout {
                    onPurchaseSuccess()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00E676))
        ) {
            Text("Pagar", color = Color.Black)
        }

        if (payUi.value.error != null) {
            Text(
                text = payUi.value.error!!,
                color = Color.Red,
                modifier = Modifier.padding(top = 12.dp)
            )
        }

        if (payUi.value.isLoading) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.Cyan)
            }
        }
    }
}

@Composable
fun SummaryRow(
    label: String,
    value: Int,
    highlight: Boolean = false
) {
    val color = if (highlight) Color.Cyan else Color.White

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = Color.Gray)
        Text(text = "$$value", color = color, fontWeight = FontWeight.Bold)
    }
}
