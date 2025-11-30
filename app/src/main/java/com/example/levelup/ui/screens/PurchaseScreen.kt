package com.example.levelup.ui.screens

import android.Manifest
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.levelup.data.dto.CardPaymentDTO
import com.example.levelup.viewmodel.CartViewModel
import com.example.levelup.viewmodel.CouponsViewModel
import com.example.levelup.viewmodel.PaymentViewModel

// Vibración
@RequiresPermission(Manifest.permission.VIBRATE)
private fun performFeedback(context: Context, isSuccess: Boolean) {

    val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        (context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager).defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    val effect = if (isSuccess) {
        VibrationEffect.createOneShot(150, VibrationEffect.DEFAULT_AMPLITUDE)
    } else {
        VibrationEffect.createWaveform(longArrayOf(0, 80, 40, 80), -1)
    }

    vibrator.vibrate(effect)
}

@Composable
fun PurchaseScreen(
    onPurchaseSuccess: () -> Unit = {}
) {
    val cartVM: CartViewModel = hiltViewModel()
    val couponVM: CouponsViewModel = hiltViewModel()
    val payVM: PaymentViewModel = hiltViewModel()

    val context = LocalContext.current

    val cartUi = cartVM.ui.collectAsState()
    val couponUi = couponVM.ui.collectAsState()
    val payUi = payVM.ui.collectAsState()

    var couponCode by remember { mutableStateOf("") }
    var cardNumber by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }
    var localError by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) { cartVM.loadCart() }

    val user = cartVM.session.getCurrentUser()
    if (user == null) {
        Text("Debes iniciar sesión", color = Color.Red)
        return
    }

    val cartItems = cartUi.value.items
    val isCartEmpty = cartItems.isEmpty()

    val subtotal = cartItems.sumOf { it.price * it.qty }
    val discountPercent = couponUi.value.coupon?.discount ?: 0
    val discountValue = (subtotal * discountPercent) / 100
    val total = subtotal - discountValue

    val isCardValid = cardNumber.length >= 12 && cardNumber.all { it.isDigit() }
    val isCvvValid = cvv.length == 3 && cvv.all { it.isDigit() }

    val isCheckoutEnabled =
        !isCartEmpty &&
                isCardValid &&
                isCvvValid &&
                !payUi.value.loading

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

        if (isCartEmpty) {
            Text(
                "🚨 El carrito está vacío.",
                color = Color.Red,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        SummaryRow("Subtotal", subtotal)

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = couponCode,
            onValueChange = { couponCode = it },
            placeholder = { Text("Código de cupón") },
            modifier = Modifier.fillMaxWidth(),
            colors = customTextFieldColors()
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

        couponUi.value.error?.let {
            Text(it, color = Color.Red, modifier = Modifier.padding(top = 8.dp))
        }

        if (discountPercent > 0) {
            SummaryRow("Descuento (${discountPercent}%)", -discountValue)
        }

        Spacer(modifier = Modifier.height(16.dp))

        SummaryRow("Total Final", total, highlight = true)

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            "Datos de Tarjeta (Simulado)",
            color = Color.White,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // TARJETA
        TextField(
            value = cardNumber,
            onValueChange = {
                if (it.all { c -> c.isDigit() }) {
                    cardNumber = it.take(16)
                }
            },
            placeholder = { Text("Número de Tarjeta (12-16 dígitos)") },
            isError = !isCardValid && cardNumber.isNotEmpty(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            colors = customTextFieldColors()
        )

        if (!isCardValid && cardNumber.isNotEmpty()) {
            Text("Debe tener mínimo 12 dígitos.", color = Color.Red)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // CVV
        TextField(
            value = cvv,
            onValueChange = {
                if (it.all { c -> c.isDigit() }) {
                    cvv = it.take(3)
                }
            },
            placeholder = { Text("CVV (3 dígitos)") },
            isError = !isCvvValid && cvv.isNotEmpty(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(0.5f),
            colors = customTextFieldColors()
        )

        if (!isCvvValid && cvv.isNotEmpty()) {
            Text("Debe tener 3 dígitos.", color = Color.Red)
        }

        Spacer(modifier = Modifier.height(20.dp))

        // BOTÓN PAGO FINAL (CORREGIDO)
        Button(
            onClick = {
                localError = null

                if (!isCheckoutEnabled) {
                    localError = "Completa los datos de pago y verifica el carrito."
                    performFeedback(context, false)
                    return@Button
                }

                val cardData = CardPaymentDTO(
                    cardNumber = cardNumber,
                    cardHolder = user.name,
                    expirationMonth = 12,
                    expirationYear = 2030,
                    cvv = cvv
                )

                payVM.checkout(
                    userId = user.id,
                    items = cartItems,
                    total = total,
                    nombreUsuario = user.name,
                    direccionEnvio = "Dirección de envío",
                    cardPaymentDTO = cardData,
                    onSuccess = {
                        // 🛑 CORRECCIÓN CLAVE: Llamar a clearCart() para vaciarlo explícitamente
                        cartVM.clearCart()
                        performFeedback(context, true)
                        onPurchaseSuccess()
                    }
                )
            },
            enabled = isCheckoutEnabled,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isCheckoutEnabled) Color(0xFF00E676) else Color.Gray
            )
        ) {
            Text("Pagar Ahora", color = Color.Black)
        }

        payUi.value.error?.let {
            Text(it, color = Color.Red, modifier = Modifier.padding(top = 12.dp))
        }

        localError?.let {
            Text(it, color = Color.Red, modifier = Modifier.padding(top = 12.dp))
        }

        if (payUi.value.loading) {
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
private fun customTextFieldColors() = TextFieldDefaults.colors(
    focusedContainerColor = Color(0xFF1A1A1A),
    unfocusedContainerColor = Color(0xFF1A1A1A),
    focusedTextColor = Color.White,
    unfocusedTextColor = Color.White,
    focusedPlaceholderColor = Color.Gray,
    unfocusedPlaceholderColor = Color.Gray
)

@Composable
fun SummaryRow(label: String, value: Int, highlight: Boolean = false) {
    val color = if (highlight) Color.Cyan else Color.White

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = Color.Gray)
        Text("$$value", color = color, fontWeight = FontWeight.Bold)
    }
}