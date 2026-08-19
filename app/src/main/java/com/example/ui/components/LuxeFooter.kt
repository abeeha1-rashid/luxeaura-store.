package com.example.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.LuxeTab
import com.example.ui.theme.*

@Composable
fun LuxeFooter(
    onNavigateTo: (LuxeTab) -> Unit,
    onOpenAdminLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val whatsappNumber = "03340634595"

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(LuxeOffWhite)
            .border(width = 1.dp, color = LuxeGoldBorder.copy(alpha = 0.6f))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Logo & Tagline
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(GoldGradient),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "L",
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    color = LuxeWhite,
                    fontSize = 20.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "LuxeAura.pk",
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                letterSpacing = 1.5.sp,
                color = LuxeCharcoal
            )

            Text(
                text = "“Elegance in Every Detail.”",
                fontFamily = FontFamily.Serif,
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                fontSize = 13.sp,
                color = LuxeGoldDark,
                textAlign = TextAlign.Center
            )
        }

        Divider(color = LuxeGoldBorder.copy(alpha = 0.5f), modifier = Modifier.fillMaxWidth(0.8f))

        // Quick Links Title & Navigation Links
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "QUICK LINKS",
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                letterSpacing = 2.sp,
                color = LuxeTextDark
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Home",
                    fontSize = 13.sp,
                    color = LuxeTextMuted,
                    modifier = Modifier.clickable { onNavigateTo(LuxeTab.HOME) }
                )
                Text(
                    text = "Jewellery",
                    fontSize = 13.sp,
                    color = LuxeTextMuted,
                    modifier = Modifier.clickable { onNavigateTo(LuxeTab.JEWELLERY) }
                )
                Text(
                    text = "Watches",
                    fontSize = 13.sp,
                    color = LuxeTextMuted,
                    modifier = Modifier.clickable { onNavigateTo(LuxeTab.WATCHES) }
                )
                Text(
                    text = "Wishlist",
                    fontSize = 13.sp,
                    color = LuxeTextMuted,
                    modifier = Modifier.clickable { onNavigateTo(LuxeTab.WISHLIST) }
                )
                Text(
                    text = "Contact",
                    fontSize = 13.sp,
                    color = LuxeTextMuted,
                    modifier = Modifier.clickable { onNavigateTo(LuxeTab.CONTACT) }
                )
            }
        }

        // WhatsApp Direct Link Card
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = LuxeWhite),
            border = androidx.compose.foundation.BorderStroke(1.dp, LuxeGoldBorder),
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .clickable {
                    val url = "https://wa.me/923340634595?text=Hello%20LuxeAura.pk%2C%20I%20would%20like%20to%20inquire%20about%20your%20luxury%20collection."
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    try {
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        // fallback to dialer
                        val dial = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$whatsappNumber"))
                        context.startActivity(dial)
                    }
                }
                .testTag("footer_whatsapp_button")
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Chat,
                    contentDescription = null,
                    tint = LuxeGoldDark,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "WhatsApp: $whatsappNumber",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
                    color = LuxeCharcoal
                )
            }
        }

        // Copyright & Discreet Admin Link
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "© 2026 LuxeAura.pk. All rights reserved.",
                fontSize = 11.sp,
                color = LuxeTextLight
            )
            
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .clickable { onOpenAdminLogin() }
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .testTag("footer_admin_link")
            ) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = LuxeGoldDark.copy(alpha = 0.5f),
                    modifier = Modifier.size(12.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Staff & Admin Access",
                    fontSize = 10.5.sp,
                    color = LuxeGoldDark.copy(alpha = 0.7f),
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
