package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LuxeColorScheme = lightColorScheme(
    primary = LuxeGold,
    onPrimary = LuxeWhite,
    primaryContainer = LuxeGoldLight,
    onPrimaryContainer = LuxeGoldDark,
    secondary = LuxeGoldDark,
    onSecondary = LuxeWhite,
    secondaryContainer = LuxeGoldGlow,
    onSecondaryContainer = LuxeCharcoal,
    tertiary = LuxeGold,
    onTertiary = LuxeWhite,
    background = LuxeWhite,
    onBackground = LuxeTextDark,
    surface = LuxeWhite,
    onSurface = LuxeTextDark,
    surfaceVariant = LuxeCream,
    onSurfaceVariant = LuxeTextMuted,
    outline = LuxeGoldBorder,
    outlineVariant = LuxeGoldBorder.copy(alpha = 0.5f)
)

@Composable
fun LuxeAuraTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LuxeColorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    LuxeAuraTheme(content = content)
}
