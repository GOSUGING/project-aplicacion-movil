package com.example.levelup.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import com.example.levelup.ui.components.AppTopBar
import com.example.levelup.viewmodel.CartViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import com.example.levelup.R

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    onNavigateToProducts: () -> Unit,
    onNavigateToCart: () -> Unit,
    cartViewModel: CartViewModel
) {
    Scaffold(
        topBar = {
            AppTopBar(
                cartViewModel = cartViewModel,
                onCartClick = onNavigateToCart,
                onMenuClick = onNavigateToProducts, // ✅ faltaba esto
                backgroundColor = Color.Black,
                contentColor = Color.White
            )
        },
        containerColor = Color.Black
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(
                text = "Bienvenidos a Level-Up Gamer",
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White
            )
            Text(
                text = "Tu destino definitivo para equipos y accesorios de juego.",
                modifier = Modifier.padding(bottom = 12.dp),
                color = Color.White
            )

            val pagerState = rememberPagerState(pageCount = { 3 })
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) { page ->
                val image = when (page) {
                    0 -> R.drawable.carousel_img_1
                    1 -> R.drawable.carousel_img_2
                    else -> R.drawable.carousel_img_3
                }
                Box(contentAlignment = Alignment.BottomStart) {
                    Image(
                        painter = painterResource(id = image),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    )
                }
            }

            Button(
                onClick = onNavigateToProducts,
                modifier = Modifier
                    .padding(top = 12.dp)
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF39FF14),
                    contentColor = Color.Black
                )
            ) {
                Text("Ver Productos")
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Blogs Destacados",
                modifier = Modifier.padding(top = 12.dp, bottom = 12.dp),
                color = Color.White
            )
        }
    }
}