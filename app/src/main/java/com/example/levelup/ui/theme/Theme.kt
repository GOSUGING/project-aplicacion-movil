package com.example.levelup.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// 🎨 Paleta de colores principal
private val DarkColors = darkColorScheme(
    primary = Color(0xFF39FF14),     // Verde fluorescente
    onPrimary = Color.Black,          // Texto sobre el verde
    secondary = Color(0xFF00CC66),    // Verde secundario más suave
    background = Color.Black,         // Fondo general
    surface = Color.Black,            // Fondo de tarjetas/superficies
    onBackground = Color.White,       // Texto sobre el fondo
    onSurface = Color.White           // Texto sobre superficies
)

// 🌙 Tema principal de Level-Up
@Composable
fun LevelUpTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColors,
        typography = AppTypography, // Usa Orbitron (titulares) y Roboto (texto)
        content = content
    )
}
