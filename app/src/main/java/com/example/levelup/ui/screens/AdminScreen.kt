package com.example.levelup.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.levelup.data.dto.PaymentDTO // DTO de Pagos
import com.example.levelup.data.dto.ProductDTO // DTO de Productos
import com.example.levelup.viewmodel.AdminViewModel
import com.example.levelup.viewmodel.SalesViewModel
import java.util.Locale

// Definición de las pestañas de la pantalla de administración
private enum class AdminTab {
    INVENTARIO,
    VENTAS
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(
    // Parámetro esencial para la navegación
    navController: NavController,
    adminViewModel: AdminViewModel = hiltViewModel(),
    salesViewModel: SalesViewModel = hiltViewModel()
) {
    var selectedTab by remember { mutableStateOf(AdminTab.INVENTARIO) }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(title = { Text("Panel de Administración") })
                AdminTabRow(
                    selectedTab = selectedTab,
                    onTabSelected = { selectedTab = it }
                )
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {

            // Botones de navegación (el de volver es el primero)
            AdminNavigationButtons(navController)

            Spacer(Modifier.height(16.dp))

            when (selectedTab) {
                AdminTab.INVENTARIO -> InventoryContent(adminViewModel)
                AdminTab.VENTAS -> SalesContent(salesViewModel)
            }
        }
    }
}

// --- NUEVA FUNCIÓN: BOTONES DE NAVEGACIÓN ---
@Composable
private fun AdminNavigationButtons(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // 1. Botón para devolverse al inicio
        Button(
            onClick = {
                navController.navigate("home") {
                    // Limpia la pila para dejar solo Home
                    popUpTo("home") { inclusive = true }
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            )
        ) {
            Text("Volver al Inicio")
        }

        // 2. Botón para ir a mi perfil
        Button(
            onClick = { navController.navigate("profile") },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF39FF14),
                contentColor = Color.Black
            )
        ) {
            Text("Mi Perfil")
        }
    }
}

// --- PESTAÑA DE NAVEGACIÓN (Se mantiene igual) ---

@Composable
private fun AdminTabRow(
    selectedTab: AdminTab,
    onTabSelected: (AdminTab) -> Unit
) {
    TabRow(selectedTabIndex = selectedTab.ordinal) {
        AdminTab.entries.forEach { tab ->
            Tab(
                selected = selectedTab == tab,
                onClick = { onTabSelected(tab) },
                text = { Text(tab.name) }
            )
        }
    }
}

// --- CONTENIDO DEL INVENTARIO (Se mantiene igual) ---

@Composable
private fun InventoryContent(viewModel: AdminViewModel) {
    val uiState by viewModel.ui.collectAsState()

    if (uiState.isLoading) {
        LoadingIndicator("Cargando Inventario...")
        return
    }

    if (uiState.error != null) {
        ErrorView(uiState.error!!, onRefresh = viewModel::loadProducts)
        return
    }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(uiState.products) { product ->
            ProductCard(product, viewModel::updateStock)
            Divider(color = Color.DarkGray)
        }
    }

    if (uiState.products.isEmpty() && !uiState.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No hay productos en el inventario.")
        }
    }
}

@Composable
private fun ProductCard(
    product: ProductDTO,
    onUpdateStock: (Long, Int) -> Unit
) {
    var stockInput by remember { mutableStateOf(product.stock.toString()) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.elevatedCardColors()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = product.name,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text("Precio: $${product.price}", color = Color.Gray)
                Text("Stock actual: ${product.stock}", color = Color.Black)
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Campo para actualizar stock
            OutlinedTextField(
                value = stockInput,
                onValueChange = { stockInput = it.filter { char -> char.isDigit() } },
                label = { Text("Nuevo Stock") },
                modifier = Modifier.width(120.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Botón de actualización
            Button(
                onClick = {
                    val newStock = stockInput.toIntOrNull()
                    if (newStock != null && newStock >= 0) {
                        onUpdateStock(product.id, newStock)
                    }
                },
                enabled = stockInput.toIntOrNull() != product.stock
            ) {
                Text("Actualizar")
            }
        }
    }
}


// --- CONTENIDO DE VENTAS (Se mantiene igual, pero con manejo de nulos implícito) ---

@Composable
private fun SalesContent(viewModel: SalesViewModel) {
    val uiState by viewModel.ui.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadSales()
    }

    if (uiState.isLoading) {
        LoadingIndicator("Cargando Ventas...")
        return
    }

    if (uiState.error != null) {
        ErrorView(uiState.error!!, onRefresh = viewModel::loadSales)
        return
    }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(uiState.sales) { payment ->
            PaymentCard(payment)
            Divider(color = Color.DarkGray)
        }
    }

    if (uiState.sales.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No hay ventas registradas.")
        }
    }
}

@Composable
private fun PaymentCard(payment: PaymentDTO) {
    // Manejo de nulos seguro basado en la estructura PaymentDTO corregida (String?)
    val statusText = payment.estado ?: "PENDIENTE"
    val userName = payment.nombreUsuario ?: "Anónimo"
    val saleDate = payment.fecha ?: "Fecha Desconocida"
    val total = payment.total // Asumiendo que total ya es un Long/Int

    // Convertimos el estado de forma segura y formateamos
    val displayStatus = statusText.lowercase(Locale.ROOT).replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString()
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                // CORRECCIÓN: Usamos el nombre de usuario de forma segura
                Text(
                    text = "ID Pago: #${payment.id} (Usuario: $userName)",
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                // CORRECCIÓN: Usamos la fecha de forma segura
                Text(
                    text = "Fecha: $saleDate",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.DarkGray
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "$${total}",
                    fontWeight = FontWeight.ExtraBold,
                    color = if (statusText.equals("PAGADO", ignoreCase = true)) Color(0xFF4CAF50) else Color(0xFFFF9800),
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = displayStatus,
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.Black
                )
            }
        }
    }
}

// --- COMPONENTES REUTILIZABLES (Se mantienen igual) ---

@Composable
private fun LoadingIndicator(message: String) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator()
        Spacer(modifier = Modifier.height(8.dp))
        Text(message)
    }
}

@Composable
private fun ErrorView(error: String, onRefresh: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "⚠️ Error: $error",
            color = Color.Red,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRefresh) {
            Text("Recargar Datos")
        }
    }
}