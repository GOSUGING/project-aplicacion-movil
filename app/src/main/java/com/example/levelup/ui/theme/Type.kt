package com.example.levelup.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.levelup.R

// === FUENTE ORBITRON ===
val Orbitron = FontFamily(
    Font(R.font.orbitron_regular, FontWeight.W400, FontStyle.Normal),
    Font(R.font.orbitron_medium, FontWeight.W500, FontStyle.Normal),
    Font(R.font.orbitron_semibold, FontWeight.W600, FontStyle.Normal),
    Font(R.font.orbitron_bold, FontWeight.W700, FontStyle.Normal),
    Font(R.font.orbitron_extrabold, FontWeight.W800, FontStyle.Normal),
    Font(R.font.orbitron_black, FontWeight.W900, FontStyle.Normal)
)

// === FUENTE ROBOTO ===
val Roboto = FontFamily(
    Font(R.font.roboto_regular, FontWeight.W400, FontStyle.Normal),
    Font(R.font.roboto_medium, FontWeight.W500, FontStyle.Normal),
    Font(R.font.roboto_bold, FontWeight.W700, FontStyle.Normal)
)

// === TIPOGRAFÍA GLOBAL ===
val AppTypography = Typography(

    // TITULOS GRANDES
    headlineLarge = TextStyle(
        fontFamily = Orbitron,
        fontWeight = FontWeight.W700,
        fontSize = 30.sp,
        color = androidx.compose.ui.graphics.Color.White
    ),

    // SUBTÍTULOS
    titleLarge = TextStyle(
        fontFamily = Orbitron,
        fontWeight = FontWeight.W600,
        fontSize = 22.sp,
        color = androidx.compose.ui.graphics.Color.White
    ),

    // TEXTO PRINCIPAL
    bodyLarge = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.W400,
        fontSize = 16.sp,
        color = androidx.compose.ui.graphics.Color.White
    ),

    // TEXTO SECUNDARIO / DESCRIPTIVO
    bodyMedium = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.W400,
        fontSize = 14.sp,
        color = androidx.compose.ui.graphics.Color(0xFFD3D3D3) // gris suave
    ),

    // BOTONES
    labelLarge = TextStyle(
        fontFamily = Orbitron,
        fontWeight = FontWeight.W700,
        fontSize = 14.sp,
        color = androidx.compose.ui.graphics.Color.Black
    )
)
