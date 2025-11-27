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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.levelup.viewmodel.PaymentsHistoryViewModel
import org.json.JSONArray

// ---------------------------------------------------------
// MODELO LOCAL PARA RAW_PAYLOAD
// ---------------------------------------------------------
data class RawItem(
    val productId: Long,
    val cantidad: Int,
    val price: Int
)

// ---------------------------------------------------------
// PARSEADOR RAW_PAYLOAD
// ---------------------------------------------------------
fun parseRawPayload(raw: String?): List<RawItem> {
    if (raw.isNullOrBlank()) return emptyList()

    val jsonArray = JSONArray(raw)
    val list = mutableListOf<RawItem>()

    for (i in 0 until jsonArray.length()) {
        val obj = jsonArray.getJSONObject(i)
        list.add(
            RawItem(
                productId = obj.optLong("productId"),
                cantidad = obj.optInt("cantidad"),
                price = obj.optInt("price")
            )
        )
    }

    return list
}

// ---------------------------------------------------------
// PANTALLA DE HISTORIAL DE PAGOS - ESTILO CYBERPUNK
// ---------------------------------------------------------
@Composable
fun PaymentsHistoryScreen(
    viewModel: PaymentsHistoryViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val ui = viewModel.ui.collectAsState()

    // Cargar pagos al abrir
    LaunchedEffect(Unit) {
        viewModel.loadPayments()
    }

    val neon = Color(0xFF39FF14)
    val cyan = Color(0xFF00FFFF)
    val magenta = Color(0xFFFF00FF)
    val bg = Color(0xFF000000)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bg)
            .padding(16.dp)
    ) {

        // -------------------------------------
        // TITULO ESTILO CYBERPUNK
        // -------------------------------------
        Text(
            "HISTORIAL DE PAGOS",
            color = neon,
            fontSize = MaterialTheme.typography.headlineMedium.fontSize,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier
                .padding(bottom = 16.dp)
                .border(
                    2.dp,
                    Brush.linearGradient(listOf(neon, cyan, magenta)),
                    RoundedCornerShape(8.dp)
                )
                .padding(12.dp)
        )

        // -------------------------------------
        // LISTA DE PAGOS
        // -------------------------------------
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(ui.value.payments) { p ->

                val itemsRaw = parseRawPayload(p.rawPayload)

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .border(
                            2.dp,
                            Brush.horizontalGradient(listOf(cyan, magenta)),
                            RoundedCornerShape(12.dp)
                        ),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF111111)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {

                    Column(Modifier.padding(16.dp)) {

                        Text("ID compra: ${p.id}", color = neon, fontWeight = FontWeight.Bold)
                        Text("Total: $${p.total}", color = Color.White)
                        Text("Estado: ${p.estado}", color = Color.Gray)
                        Text("Fecha: ${p.fecha}", color = Color.Gray)

                        Spacer(Modifier.height(8.dp))
                        Text("Productos:", color = magenta, fontWeight = FontWeight.Bold)

                        if (itemsRaw.isEmpty()) {
                            Text("• Sin productos (payload vacío)", color = Color.Gray)
                        } else {
                            itemsRaw.forEach { item ->
                                Text(
                                    "• ID ${item.productId} - ${item.cantidad} un. - $${item.price}",
                                    color = cyan
                                )
                            }
                        }
                    }
                }
            }
        }

        // -------------------------------------
        // BOTÓN VOLVER ESTILO CYBERPUNK
        // -------------------------------------
        Button(
            onClick = onBack,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = neon)
        ) {
            Icon(Icons.Default.ArrowBack, contentDescription = "volver", tint = Color.Black)
            Spacer(Modifier.width(6.dp))
            Text("VOLVER", color = Color.Black, fontWeight = FontWeight.Bold)
        }
    }
}
