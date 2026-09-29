package com.example.myapplication.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.myapplication.R

// Define Among Us font family
val AmongUsFontFamily = FontFamily(
    Font(R.font.amongus_font, FontWeight.Normal)
)

// Set of Material typography styles to use your custom Among Us font across the app
val Typography = Typography(
    displayLarge = TextStyle(fontFamily = AmongUsFontFamily),
    displayMedium = TextStyle(fontFamily = AmongUsFontFamily),
    displaySmall = TextStyle(fontFamily = AmongUsFontFamily),
    headlineLarge = TextStyle(fontFamily = AmongUsFontFamily),
    headlineMedium = TextStyle(fontFamily = AmongUsFontFamily),
    headlineSmall = TextStyle(fontFamily = AmongUsFontFamily),
    titleLarge = TextStyle(fontFamily = AmongUsFontFamily),
    titleMedium = TextStyle(fontFamily = AmongUsFontFamily),
    titleSmall = TextStyle(fontFamily = AmongUsFontFamily),
    bodyLarge = TextStyle(
        fontFamily = AmongUsFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(fontFamily = AmongUsFontFamily),
    bodySmall = TextStyle(fontFamily = AmongUsFontFamily),
    labelLarge = TextStyle(fontFamily = AmongUsFontFamily),
    labelMedium = TextStyle(fontFamily = AmongUsFontFamily),
    labelSmall = TextStyle(fontFamily = AmongUsFontFamily)
)
