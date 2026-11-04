package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Primary Branding & Accent Colors
val RoyalPurple = Color(0xFF6C5CE7)
val DeepIndigo = Color(0xFF4834D4)
val NeonPurple = Color(0xFFA29BFE)
val BrightGold = Color(0xFFFFD700)
val RadiantGold = Color(0xFFFFB800)
val DarkGold = Color(0xFFE67E22)

// Background & Surface
val DarkBackground = Color(0xFF090D1E)
val DarkSurface = Color(0xFF111736)
val DarkSurfaceVariant = Color(0xFF1B234F)
val DarkSurfaceCard = Color(0xFF161E42)
val CardBorderStroke = Color(0xFF2C3970)

// Semantic
val SuccessEmerald = Color(0xFF00E676)
val WarningAmber = Color(0xFFFF9100)
val DangerCoral = Color(0xFFFF5252)
val InfoSky = Color(0xFF00B0FF)

// Text Colors
val TextHigh = Color(0xFFF8FAFC)
val TextMedium = Color(0xFFCBD5E1)
val TextMuted = Color(0xFF8896B3)

// Luxury Gradients
val GoldGradient = Brush.horizontalGradient(
    colors = listOf(Color(0xFFFFD700), Color(0xFFFF9100))
)

val PurpleGradient = Brush.horizontalGradient(
    colors = listOf(Color(0xFF6C5CE7), Color(0xFF4834D4))
)

val CardGradient = Brush.linearGradient(
    colors = listOf(Color(0xFF1B234F), Color(0xFF111736))
)

val HeroBannerGradient = Brush.linearGradient(
    colors = listOf(Color(0xFF2D1B69), Color(0xFF111736), Color(0xFF0A1128))
)

val CashGradient = Brush.horizontalGradient(
    colors = listOf(Color(0xFF00C853), Color(0xFF00B4D8))
)
