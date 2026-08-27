package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = AshokaBlueLight,
    onPrimary = SovereignNavy,
    primaryContainer = SovereignNavyLight,
    onPrimaryContainer = AshokaBlueLight,
    secondary = BharatSaffron,
    onSecondary = Color.White,
    secondaryContainer = BharatSaffron.copy(alpha = 0.2f),
    onSecondaryContainer = BharatSaffronLight,
    tertiary = BharatGreenLight,
    onTertiary = Color.White,
    tertiaryContainer = BharatGreen.copy(alpha = 0.2f),
    onTertiaryContainer = BharatGreenLight,
    background = SovereignSurfaceDark,
    onBackground = TextPrimaryDark,
    surface = SovereignCardDark,
    onSurface = TextPrimaryDark,
    surfaceVariant = SovereignNavyLight,
    onSurfaceVariant = TextSecondaryDark,
    error = StatusError,
    onError = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = SovereignNavy,
    onPrimary = Color.White,
    primaryContainer = AshokaBlue.copy(alpha = 0.12f),
    onPrimaryContainer = SovereignNavy,
    secondary = BharatSaffron,
    onSecondary = Color.White,
    secondaryContainer = BharatSaffronLight.copy(alpha = 0.15f),
    onSecondaryContainer = Color(0xFF7C2D12),
    tertiary = BharatGreen,
    onTertiary = Color.White,
    tertiaryContainer = BharatGreen.copy(alpha = 0.15f),
    onTertiaryContainer = Color(0xFF064E3B),
    background = SovereignBgLight,
    onBackground = TextPrimaryLight,
    surface = SovereignSurfaceLight,
    onSurface = TextPrimaryLight,
    surfaceVariant = SovereignSurfaceVariantLight,
    onSurfaceVariant = TextSecondaryLight,
    error = StatusError,
    onError = Color.White
)

private val HighContrastColorScheme = lightColorScheme(
    primary = Color(0xFF000000),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFF002244),
    onPrimaryContainer = Color(0xFFFFFFFF),
    secondary = Color(0xFFD97706),
    onSecondary = Color(0xFF000000),
    tertiary = Color(0xFF006400),
    onTertiary = Color(0xFFFFFFFF),
    background = Color(0xFFFFFFFF),
    onBackground = Color(0xFF000000),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF000000),
    surfaceVariant = Color(0xFFEEEEEE),
    onSurfaceVariant = Color(0xFF000000),
    error = Color(0xFFB00020),
    onError = Color(0xFFFFFFFF)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    isHighContrast: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        isHighContrast -> HighContrastColorScheme
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
