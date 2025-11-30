package com.example.levelup.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PurchaseResultScreen(
    onBackHome: () -> Unit,
    onViewBills: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Compra realizada con éxito")

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = onBackHome) {
            Text("Volver al inicio")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(onClick = onViewBills) {
            Text("Ver mis compras")
        }
    }
}
