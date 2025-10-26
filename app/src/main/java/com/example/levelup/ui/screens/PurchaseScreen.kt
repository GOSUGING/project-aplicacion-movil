// Archivo: PurchaseScreen.kt
package com.example.levelup.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.levelup.viewmodel.CartViewModel
import java.text.NumberFormat
import java.util.Locale
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope

// -----------------------------
// Visual transformations (formato visual de inputs)
// -----------------------------

// Muestra el número como "1234 5678 9012 3456" mientras mantiene solo dígitos en el estado
private class CreditCardVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val raw = text.text.filter(Char::isDigit).take(16)

        val out = buildString {
            raw.forEachIndexed { i, c ->
                append(c)
                if ((i + 1) % 4 == 0 && i != 15) append(' ')
            }
        }

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                val o = offset.coerceIn(0, 16)
                return when {
                    o <= 4 -> o
                    o <= 8 -> o + 1
                    o <= 12 -> o + 2
                    else -> minOf(o + 3, 19)
                }
            }

            override fun transformedToOriginal(offset: Int): Int {
                val t = offset.coerceIn(0, 19)
                return when {
                    t <= 4 -> t
                    t <= 9 -> t - 1
                    t <= 14 -> t - 2
                    else -> minOf(t - 3, 16)
                }
            }
        }

        return TransformedText(AnnotatedString(out), offsetMapping)
    }
}

// Muestra la fecha como "MM/AA" mientras el estado mantiene solo 4 dígitos "MMAA"
private class ExpiryDateVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val raw = text.text.filter(Char::isDigit).take(4)
        val formatted =
            if (raw.length <= 2) raw else raw.substring(0, 2) + "/" + raw.substring(2)
        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int = when {
                offset <= 2 -> offset
                offset in 3..4 -> offset + 1
                else -> 5
            }

            override fun transformedToOriginal(offset: Int): Int = when {
                offset <= 2 -> offset
                offset in 3..5 -> offset - 1
                else -> 4
            }
        }
        return TransformedText(AnnotatedString(formatted), offsetMapping)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PurchaseScreen(
    navController: NavController,
    cartViewModel: CartViewModel
) {
    val items by cartViewModel.cartItems.collectAsState()
    val total by remember(items) { derivedStateOf { cartViewModel.totalPrice() } }

    // Estado del formulario
    var cardOwner by remember { mutableStateOf("") }
    var cardNumber by remember { mutableStateOf("") }
    var cardNumberTF by remember { mutableStateOf(TextFieldValue("")) }
    var expiry by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }

    // Validaciones
    val ownerValid = cardOwner.isNotBlank()
    val numberValid = cardNumber.length == 16
    val mm = expiry.take(2).toIntOrNull()
    val expiryValid = expiry.length == 4 && mm != null && mm in 1..12
    val cvvValid = cvv.length in 3..4
    val isFormValid = ownerValid && numberValid && expiryValid && cvvValid

    val clLocale = remember { Locale("es", "CL") }
    val nf = remember { NumberFormat.getCurrencyInstance(clLocale) }

    val snackbar = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Finalizar compra", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color(0xFF39FF14)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
            )
        },
        snackbarHost = { SnackbarHost(snackbar) },
        containerColor = Color.Black,
        bottomBar = {
            Surface(color = Color.Black) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, bottom = 12.dp, top = 8.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        enabled = isFormValid && items.isNotEmpty(),
                        onClick = {
                            items.forEach { cartViewModel.removeFromCart(it.product.id) }
                            scope.launch {
                                snackbar.showSnackbar("La compra se ha realizado con éxito")
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF39FF14),
                            contentColor = Color.Black,
                            disabledContainerColor = Color.DarkGray,
                            disabledContentColor = Color.Gray
                        )
                    ) {
                        Text("Terminar compra")
                    }
                }
            }
        }
    ) { inner ->
        Row(
            modifier = Modifier
                .padding(inner)
                .fillMaxSize()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Resumen del carrito
            Card(
                modifier = Modifier.weight(1f).fillMaxHeight(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A))
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text(
                        "Resumen de tu compra",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.White
                    )
                    HorizontalDivider(color = Color.Gray, modifier = Modifier.padding(vertical = 8.dp))

                    if (items.isEmpty()) {
                        Text("Tu carrito está vacío", color = Color.Gray)
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.weight(1f, fill = false)
                        ) {
                            items(items, key = { it.product.id }) { it ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        "${it.product.name} (x${it.quantity})",
                                        color = Color.White,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Text(nf.format(it.product.price), color = Color(0xFF39FF14))
                                }
                            }
                        }
                        HorizontalDivider(color = Color.Gray, modifier = Modifier.padding(vertical = 8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Total:", style = MaterialTheme.typography.titleLarge, color = Color.White)
                            Text(
                                nf.format(total),
                                style = MaterialTheme.typography.titleLarge,
                                color = Color(0xFF39FF14)
                            )
                        }
                    }
                }
            }

            // Formulario de tarjeta
            Card(
                modifier = Modifier.weight(1f).fillMaxHeight(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("Datos de la tarjeta", style = MaterialTheme.typography.headlineSmall, color = Color.White)

                    OutlinedTextField(
                        value = cardOwner,
                        onValueChange = { cardOwner = it },
                        label = { Text("Nombre de titular") },
                        singleLine = true,
                        isError = !ownerValid && cardOwner.isNotEmpty(),
                        supportingText = {
                            if (!ownerValid && cardOwner.isNotEmpty())
                                Text("Ingrese el nombre del titular", color = Color(0xFFFF6B6B))
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = appTextFieldColors()
                    )
                    OutlinedTextField(
                        value = cardNumberTF,
                        onValueChange = { tfv ->
                            val digits = tfv.text.filter(Char::isDigit).take(16)
                            val newSelection = tfv.selection.start.coerceAtMost(digits.length)

                            if (digits != cardNumber) {
                                cardNumber = digits
                                cardNumberTF = TextFieldValue(
                                    text = digits,
                                    selection = TextRange(newSelection)
                                )
                            } else {
                                cardNumberTF = tfv.copy(text = digits)
                            }
                        },
                        label = { Text("Número de tarjeta") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        visualTransformation = CreditCardVisualTransformation(),
                        isError = !numberValid && cardNumber.isNotEmpty(),
                        supportingText = {
                            val len = cardNumber.length
                            Text(
                                "$len/16 dígitos",
                                color = if (numberValid || len == 0) Color.Gray else Color(0xFFFF6B6B)
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = appTextFieldColors()
                    )

                    OutlinedTextField(
                        value = expiry,
                        onValueChange = { input ->
                            expiry = input.filter(Char::isDigit).take(4)
                        },
                        label = { Text("Vencimiento (MM/AA)") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        visualTransformation = ExpiryDateVisualTransformation(),
                        isError = !expiryValid && expiry.isNotEmpty(),
                        supportingText = {
                            if (!expiryValid && expiry.isNotEmpty())
                                Text("Formato: MM/AA (mes 01..12)", color = Color(0xFFFF6B6B))
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = appTextFieldColors()
                    )

                    OutlinedTextField(
                        value = cvv,
                        onValueChange = { cvv = it.filter(Char::isDigit).take(4) },
                        label = { Text("CVV") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                        visualTransformation = PasswordVisualTransformation(),
                        isError = !cvvValid && cvv.isNotEmpty(),
                        supportingText = {
                            if (!cvvValid && cvv.isNotEmpty())
                                Text("3 o 4 dígitos", color = Color(0xFFFF6B6B))
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = appTextFieldColors()
                    )

                    Spacer(Modifier.height(8.dp))
                }
            }
        }
    }
}
