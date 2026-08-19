package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// LuxeAura Editorial Aesthetic Champagne Gold & Pure White Palette
val LuxeGold = Color(0xFFD4AF37)
val LuxeGoldDark = Color(0xFFB49331)
val LuxeGoldLight = Color(0xFFE5C76B)
val LuxeGoldGlow = Color(0xFFFFF9E6)
val LuxeGoldBorder = Color(0xFFF0EEE9)
val LuxeGoldBorderAccent = Color(0xFFE5C76B)

val LuxeWhite = Color(0xFFFFFFFF)
val LuxeOffWhite = Color(0xFFFAF9F6)
val LuxeCream = Color(0xFFF9F5F0)

val LuxeCharcoal = Color(0xFF171717)
val LuxeTextDark = Color(0xFF262626)
val LuxeTextMuted = Color(0xFF737373)
val LuxeTextLight = Color(0xFFA3A3A3)

val LuxeSuccess = Color(0xFF25D366) // WhatsApp Brand Green
val LuxeDiscountBadge = Color(0xFFD4AF37) // Editorial Gold for Discount Badges
val LuxeHeartRed = Color(0xFFD4AF37) // Editorial Accent Heart

// Editorial Luxury Gradients
val GoldGradient = Brush.horizontalGradient(
    colors = listOf(
        Color(0xFFD4AF37),
        Color(0xFFE5C76B)
    )
)

val EditorialHeroBgGradient = Brush.verticalGradient(
    colors = listOf(
        Color(0xFFF9F5F0),
        Color(0xFFFFFFFF)
    )
)

val ShimmerGoldGradient = Brush.linearGradient(
    colors = listOf(
        Color(0xFFD4AF37),
        Color(0xFFFFF7DB),
        Color(0xFFE5C76B)
    )
)

val HeroOverlayGradient = Brush.verticalGradient(
    colors = listOf(
        Color.Transparent,
        Color(0x99171717)
    )
)
