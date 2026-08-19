package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun OpeningWelcomeAnimation(
    onAnimationComplete: () -> Unit
) {
    val fullText = "Welcome to LuxeAura.pk"
    var displayedCharsCount by remember { mutableIntStateOf(0) }
    var isFinishedTyping by remember { mutableStateOf(false) }

    val infiniteTransition = rememberInfiniteTransition(label = "luxury_glow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow_alpha"
    )

    val shimmerScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shimmer_scale"
    )

    // Typewriter effect
    LaunchedEffect(Unit) {
        delay(400)
        for (i in 1..fullText.length) {
            displayedCharsCount = i
            delay(90) // comfortable typing pace
        }
        isFinishedTyping = true
        delay(1200) // linger briefly for luxury impact
        onAnimationComplete()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LuxeWhite)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        // Luxury decorative ambient gold circle
        Box(
            modifier = Modifier
                .size(280.dp)
                .scale(shimmerScale)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            LuxeGoldLight.copy(alpha = glowAlpha),
                            LuxeGoldGlow.copy(alpha = 0.2f),
                            Color.Transparent
                        )
                    )
                )
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Sparkling Luxury Icon
            Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = null,
                tint = LuxeGold,
                modifier = Modifier
                    .size(44.dp)
                    .scale(shimmerScale)
                    .padding(bottom = 16.dp)
            )

            // Cursive / Calligraphic Typography with Golden Gradient
            val currentDisplayed = fullText.take(displayedCharsCount)
            Text(
                text = currentDisplayed,
                fontFamily = FontFamily.Cursive,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold,
                fontSize = 38.sp,
                lineHeight = 46.sp,
                textAlign = TextAlign.Center,
                color = LuxeGoldDark,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Subtitle Tagline with subtle fade
            AnimatedVisibility(
                visible = isFinishedTyping,
                enter = fadeIn(tween(600)) + slideInVertically(initialOffsetY = { 20 })
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Elegance in Every Detail",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Light,
                        fontSize = 15.sp,
                        letterSpacing = 3.sp,
                        color = LuxeTextMuted,
                        textAlign = TextAlign.Center
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Box(
                        modifier = Modifier
                            .width(80.dp)
                            .height(1.5.dp)
                            .background(GoldGradient)
                    )
                }
            }
        }

        // Skip action in top right
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 40.dp, end = 8.dp),
            contentAlignment = Alignment.TopEnd
        ) {
            Text(
                text = "Enter Store →",
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 1.sp,
                color = LuxeGoldDark,
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(LuxeGoldGlow)
                    .clickable { onAnimationComplete() }
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
    }
}
