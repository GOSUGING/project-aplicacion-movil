// En UiCommons.kt
package com.example.levelup.ui.screens

import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * Define los colores personalizados para los OutlinedTextField de la aplicación.
 */
@Composable
fun appTextFieldColors() = OutlinedTextFieldDefaults.colors(
    // Colores del texto
    focusedTextColor = Color.White,
    unfocusedTextColor = Color.White,
    disabledTextColor = Color.White.copy(alpha = 0.7f),

    // Colores del borde
    focusedBorderColor = Color(0xFF39FF14),
    unfocusedBorderColor = Color.DarkGray,
    errorBorderColor = Color(0xFFFF6B6B),

    // Colores de la etiqueta (label)
    focusedLabelColor = Color(0xFF39FF14),
    unfocusedLabelColor = Color.Gray,

    // Color del cursor
    cursorColor = Color(0xFF39FF14),

    // Color del contenedor (fondo del campo de texto)
    focusedContainerColor = Color.Transparent,
    unfocusedContainerColor = Color.Transparent,
    disabledContainerColor = Color.Transparent,
    errorContainerColor = Color.Transparent
)
