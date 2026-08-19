package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.LuxeTab
import com.example.ui.components.LuxeFooter
import com.example.ui.theme.*

@Composable
fun ContactScreen(
    onSendMessage: (String, String, String) -> Unit,
    onNavigateTo: (LuxeTab) -> Unit,
    onOpenAdminLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val whatsappNumber = "03340634595"

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var isSubmitted by remember { mutableStateOf(false) }

    LazyColumn(
        contentPadding = PaddingValues(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .fillMaxSize()
            .background(LuxeWhite)
            .testTag("contact_screen")
    ) {
        // Header
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "LUXURY CONCIERGE",
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    letterSpacing = 2.sp,
                    color = LuxeGoldDark
                )
                Text(
                    text = "Contact Us",
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = LuxeCharcoal
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Our luxury advisors are at your service for bespoke inquiries and order consultations.",
                    fontSize = 12.5.sp,
                    lineHeight = 18.sp,
                    color = LuxeTextMuted,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }

        // WhatsApp Highlight Card (Primary CTA)
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = LuxeGoldGlow),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, LuxeGold),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .testTag("contact_whatsapp_card")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(LuxeSuccess.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Chat,
                            contentDescription = null,
                            tint = LuxeSuccess,
                            modifier = Modifier.size(30.dp)
                        )
                    }

                    Text(
                        text = "Instant VIP WhatsApp Assistance",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = LuxeCharcoal
                    )

                    Text(
                        text = whatsappNumber,
                        fontFamily = FontFamily.Default,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        letterSpacing = 1.5.sp,
                        color = LuxeGoldDark
                    )

                    Text(
                        text = "Chat directly with our luxury styling specialists for customized gift boxes, sizing guidance, and real-time order tracking.",
                        fontSize = 12.sp,
                        lineHeight = 17.sp,
                        color = LuxeTextDark,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Button(
                        onClick = {
                            val url = "https://wa.me/923340634595?text=Hello%20LuxeAura.pk%2C%20I%20would%20like%20to%20inquire%20about%20your%20luxury%20collection."
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            try {
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                val dial = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$whatsappNumber"))
                                context.startActivity(dial)
                            }
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = LuxeSuccess,
                            contentColor = LuxeWhite
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("contact_whatsapp_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Chat,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Contact us on WhatsApp",
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Contact Form Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = LuxeWhite),
                border = androidx.compose.foundation.BorderStroke(1.dp, LuxeGoldBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .testTag("contact_form_card")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "SEND AN INQUIRY",
                        fontFamily = FontFamily.Default,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        letterSpacing = 1.5.sp,
                        color = LuxeGoldDark
                    )

                    Text(
                        text = "Leave Us a Message",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = LuxeCharcoal
                    )

                    if (isSubmitted) {
                        Card(
                            shape = RoundedCornerShape(10.dp),
                            colors = CardDefaults.cardColors(containerColor = LuxeGoldLight),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = LuxeGoldDark
                                )
                                Text(
                                    text = "Thank you for contacting LuxeAura.pk! Our team will respond to your email promptly.",
                                    fontSize = 12.5.sp,
                                    color = LuxeCharcoal
                                )
                            }
                        }
                    }

                    // Name
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Your Name *") },
                        placeholder = { Text("Enter full name") },
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = LuxeGold,
                            unfocusedBorderColor = LuxeGoldBorder
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("contact_name_field")
                    )

                    // Email
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email Address *") },
                        placeholder = { Text("name@example.com") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = LuxeGold,
                            unfocusedBorderColor = LuxeGoldBorder
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("contact_email_field")
                    )

                    // Message
                    OutlinedTextField(
                        value = message,
                        onValueChange = { message = it },
                        label = { Text("Your Message *") },
                        placeholder = { Text("How may we assist your luxury purchase?") },
                        maxLines = 4,
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = LuxeGold,
                            unfocusedBorderColor = LuxeGoldBorder
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("contact_message_field")
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    val isFormValid = name.isNotBlank() && email.isNotBlank() && message.isNotBlank()

                    Button(
                        onClick = {
                            onSendMessage(name.trim(), email.trim(), message.trim())
                            isSubmitted = true
                            name = ""
                            email = ""
                            message = ""
                        },
                        enabled = isFormValid,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = LuxeGold,
                            contentColor = LuxeWhite
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("contact_send_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Send Message",
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Boutique Information Cards
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ContactInfoItem(
                    icon = Icons.Outlined.LocationOn,
                    title = "Flagship Showroom & Operations",
                    value = "Gulberg III / DHA Phase 5, Lahore & Clifton, Karachi, Pakistan"
                )
                ContactInfoItem(
                    icon = Icons.Outlined.Schedule,
                    title = "Support Hours",
                    value = "Monday – Sunday: 10:00 AM – 11:00 PM PKT"
                )
                ContactInfoItem(
                    icon = Icons.Outlined.Email,
                    title = "Official Concierge Email",
                    value = "concierge@luxeaura.pk"
                )
            }
        }

        // Footer
        item {
            LuxeFooter(
                onNavigateTo = onNavigateTo,
                onOpenAdminLogin = onOpenAdminLogin,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}

@Composable
private fun ContactInfoItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    value: String
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = LuxeOffWhite),
        border = androidx.compose.foundation.BorderStroke(1.dp, LuxeGoldBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(LuxeGoldLight),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = LuxeGoldDark,
                    modifier = Modifier.size(18.dp)
                )
            }

            Column {
                Text(
                    text = title,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                    color = LuxeCharcoal
                )
                Text(
                    text = value,
                    fontSize = 11.5.sp,
                    color = LuxeTextMuted
                )
            }
        }
    }
}
