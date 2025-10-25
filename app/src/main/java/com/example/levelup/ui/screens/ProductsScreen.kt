package com.example.levelup.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.levelup.R
import com.example.levelup.model.Product
import com.example.levelup.viewmodel.CartViewModel
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.*



private val CATEGORIES = listOf("consolas", "juegos", "accesorios", "ropa")

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProductsScreen(
    navController: NavController,
    cartViewModel: CartViewModel,
    initialCategory: String? = null   // <- opcional: se inyecta desde la ruta
) {
    val allProducts = remember { sampleProducts() }

    // Normaliza categoría inicial como en ProductsPages
    val startCat = remember(initialCategory) {
        val c = initialCategory?.lowercase().orEmpty()
        if (CATEGORIES.contains(c)) c else ""
    }
    var categoria by remember { mutableStateOf(startCat) }

    val productosFiltrados = remember(categoria, allProducts) {
        if (categoria.isBlank()) allProducts else allProducts.filter { it.category == categoria }
    }

    val titulo = if (categoria.isBlank()) "Tienda Level-Up!" else
        "Categoría: ${categoria.replaceFirstChar { it.titlecase() }}"

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val money = remember { NumberFormat.getCurrencyInstance(Locale("es", "CL")) }

    Scaffold(
        scaffoldState = scaffoldState,
        backgroundColor = Color.Black,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = false }
                        launchSingleTop = true
                    }
                },
                backgroundColor = Color(0xFF39FF14),
                contentColor = Color.Black
            ) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Volver")
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        snackbarHost = {
            SnackbarHost(it) { data ->
                Snackbar(
                    snackbarData = data,
                    backgroundColor = Color(0xFF111111),
                    contentColor = Color.White
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .background(Color.Black)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Título y botón "Volver a Categorías"
            Column(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = titulo,
                    style = MaterialTheme.typography.h6,
                    color = Color(0xFF39FF14),
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(8.dp))

                OutlinedButton(
                    onClick = { navController.navigate("categories") },
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFF39FF14)
                    ),
                    border = ButtonDefaults.outlinedBorder
                ) {
                    Text("Volver a Categorías")
                }
            }

            // Grid de productos
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxSize(),
                contentPadding = PaddingValues(bottom = 88.dp, top = 4.dp)
            ) {
                items(productosFiltrados, key = { it.id }) { p ->
                    Card(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth(),
                        backgroundColor = Color(0xFF111111),
                        elevation = 6.dp
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(10.dp)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            if (p.imageUrl != null) {
                                AsyncImage(
                                    model = p.imageUrl,
                                    contentDescription = p.name,
                                    modifier = Modifier
                                        .height(120.dp)
                                        .fillMaxWidth()
                                )
                            } else if (p.imageRes != null) {
                                AsyncImage(
                                    model = p.imageRes,
                                    contentDescription = p.name,
                                    modifier = Modifier
                                        .height(120.dp)
                                        .fillMaxWidth()
                                )
                            }

                            Spacer(Modifier.height(8.dp))

                            Text(p.name, style = MaterialTheme.typography.subtitle1, color = Color.White)
                            Spacer(Modifier.height(2.dp))
                            Text(p.description, style = MaterialTheme.typography.body2, color = Color(0xFFD3D3D3))
                            Spacer(Modifier.height(6.dp))
                            Text(
                                money.format(p.price),
                                style = MaterialTheme.typography.body1,
                                color = Color(0xFF39FF14),
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(Modifier.height(8.dp))

                            Button(
                                onClick = {
                                    cartViewModel.addToCart(p)
                                    scope.launch {
                                        val r = scaffoldState.snackbarHostState.showSnackbar(
                                            message = "Agregado: ${p.name}",
                                            actionLabel = "Ver carrito",
                                            duration = SnackbarDuration.Short
                                        )
                                        if (r == SnackbarResult.ActionPerformed) {
                                            navController.navigate("cart")
                                        }
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = Color(0xFF39FF14),
                                    contentColor = Color.Black
                                )
                            ) { Text("Agregar al Carrito") }
                        }
                    }
                }
            }
        }
    }
}

/** Adapta imageRes a tus drawables (o usa imageUrl). Agregué `category` como en React. */
private fun sampleProducts(): List<Product> = listOf(
    Product(1, "Catán", "Juego de estrategia para 3-4 jugadores.", 29990, imageUrl = null, imageRes = R.drawable.catan,     category = "juegos"),
    Product(2, "Carcassonne", "Juego de colocación de fichas para 2-5 jugadores.", 24990, imageUrl = null, imageRes = R.drawable.carcassonne, category = "juegos"),
    Product(3, "Control Xbox Series X", "Control inalámbrico para Xbox y PC.", 59990, imageUrl = null, imageRes = R.drawable.control_xbox, category = "accesorios"),
    Product(4, "Auriculares HyperX Cloud II", "Sonido envolvente con micrófono desmontable.", 24990, imageUrl = null, imageRes = R.drawable.hyperx_cloud, category = "accesorios"),
    Product(5, "Play Station 5", "Consola de última generación de Sony.", 549990, imageUrl = null, imageRes = R.drawable.ps5, category = "consolas"),
    Product(6, "PC Gamer ASUS ROG", "Computadora gamer potente.", 1299990, imageUrl = null, imageRes = R.drawable.rogstrix, category = "consolas"),
    Product(7, "Silla Gamer Secretlab Titan", "Máxima comodidad y soporte ergonómico.", 349990, imageUrl = null, imageRes = R.drawable.silla_gamer, category = "accesorios"),
    Product(8, "Mouse Logitech G502 HERO", "Sensor óptico y 11 botones programables.", 49990, imageUrl = null, imageRes = R.drawable.mouse_logitech, category = "accesorios"),
    Product(9, "Mousepad Razer Chroma", "Superficie microtexturizada con iluminación RGB.", 29990, imageUrl = null, imageRes = R.drawable.mousepad_razer, category = "accesorios"),
    Product(10, "Polera Level-Up", "Polera de algodón personalizada para gamers.", 14990, imageUrl = null, imageRes = R.drawable.polera_gamer, category = "ropa"),
)
