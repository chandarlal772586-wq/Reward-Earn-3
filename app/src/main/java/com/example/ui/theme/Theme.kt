package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val RewardColorScheme = darkColorScheme(
    primary = RoyalPurple,
    onPrimary = TextHigh,
    primaryContainer = DeepIndigo,
    onPrimaryContainer = TextHigh,
    secondary = RadiantGold,
    onSecondary = DarkBackground,
    secondaryContainer = DarkGold,
    onSecondaryContainer = TextHigh,
    tertiary = NeonPurple,
    onTertiary = DarkBackground,
    background = DarkBackground,
    onBackground = TextHigh,
    surface = DarkSurface,
    onSurface = TextHigh,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = TextMedium,
    error = DangerCoral,
    onError = TextHigh,
    outline = CardBorderStroke,
    outlineVariant = DarkSurfaceCard
)

@Composable
fun RewardEarn3Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = RewardColorScheme,
        typography = Typography,
        content = content
    )
}
