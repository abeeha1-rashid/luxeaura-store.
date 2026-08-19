package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.Product
import com.example.ui.LuxeTab
import com.example.ui.components.LuxeFooter
import com.example.ui.components.ProductCard
import com.example.ui.theme.*

@Composable
fun HomeScreen(
    featuredProducts: List<Product>,
    isWishlisted: (Long) -> Boolean,
    onWishlistToggle: (Product) -> Unit,
    onAddToCart: (Product) -> Unit,
    onViewDetails: (Product) -> Unit,
    onNavigateTo: (LuxeTab) -> Unit,
    onOpenAdminLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
            .fillMaxSize()
            .background(LuxeWhite)
    ) {
        // 1. Editorial Hero Section
        item(span = { GridItemSpan(2) }) {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = LuxeCream),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, LuxeGoldBorder, RoundedCornerShape(24.dp))
                    .testTag("home_hero_banner")
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp, horizontal = 16.dp)
                ) {
                    // Decorative glow circle in background
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .offset(x = 20.dp, y = 20.dp)
                            .size(140.dp)
                            .clip(CircleShape)
                            .background(LuxeGoldLight.copy(alpha = 0.25f))
                    )

                    // Season Capsule (Top Right)
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .clip(RoundedCornerShape(20.dp))
                            .background(LuxeWhite.copy(alpha = 0.8f))
                            .border(1.dp, LuxeGoldBorder, RoundedCornerShape(20.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "SUMMER '24",
                            fontFamily = FontFamily.Default,
                            fontWeight = FontWeight.Bold,
                            fontSize = 9.sp,
                            letterSpacing = 1.5.sp,
                            color = LuxeGoldDark
                        )
                    }

                    // Centered Editorial Content
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Elegance in",
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Normal,
                            fontSize = 32.sp,
                            color = LuxeTextDark,
                            textAlign = TextAlign.Center
                        )

                        Text(
                            text = "Every Detail",
                            fontFamily = FontFamily.Serif,
                            fontStyle = FontStyle.Italic,
                            fontWeight = FontWeight.Bold,
                            fontSize = 34.sp,
                            color = LuxeGold,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "TIMELESS JEWELLERY & TIMEPIECES",
                            fontFamily = FontFamily.Default,
                            fontWeight = FontWeight.Medium,
                            fontSize = 9.5.sp,
                            letterSpacing = 2.sp,
                            color = LuxeTextMuted,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        // Editorial Pill Button Pair
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                onClick = { onNavigateTo(LuxeTab.JEWELLERY) },
                                shape = RoundedCornerShape(50.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = LuxeGold,
                                    contentColor = LuxeWhite
                                ),
                                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                                contentPadding = PaddingValues(horizontal = 22.dp, vertical = 10.dp),
                                modifier = Modifier.testTag("hero_shop_jewellery_btn")
                            ) {
                                Text(
                                    text = "Shop Now",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            OutlinedButton(
                                onClick = { onNavigateTo(LuxeTab.WATCHES) },
                                shape = RoundedCornerShape(50.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, LuxeGold),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = LuxeGoldDark
                                ),
                                contentPadding = PaddingValues(horizontal = 22.dp, vertical = 10.dp),
                                modifier = Modifier.testTag("hero_explore_watches_btn")
                            ) {
                                Text(
                                    text = "Explore",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }
        }

        // 2. Editorial Section Header (border-l-4 border-[#D4AF37] pl-3)
        item(span = { GridItemSpan(2) }) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .width(4.dp)
                            .height(22.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(LuxeGold)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "New Arrivals",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 19.sp,
                        color = LuxeTextDark
                    )
                }

                Text(
                    text = "VIEW ALL",
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    letterSpacing = 1.sp,
                    color = LuxeGold,
                    modifier = Modifier
                        .clickable { onNavigateTo(LuxeTab.JEWELLERY) }
                        .padding(4.dp)
                )
            }
        }

        // 3. Featured / New Arrivals Products Grid
        items(
            items = featuredProducts.take(6),
            key = { it.id }
        ) { product ->
            ProductCard(
                product = product,
                isWishlisted = isWishlisted(product.id),
                onWishlistToggle = onWishlistToggle,
                onAddToCart = onAddToCart,
                onViewDetails = onViewDetails
            )
        }

        // 4. Jewellery Spotlight Banner
        item(span = { GridItemSpan(2) }) {
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = LuxeCream),
                border = androidx.compose.foundation.BorderStroke(1.dp, LuxeGoldBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable { onNavigateTo(LuxeTab.JEWELLERY) }
                    .testTag("home_jewellery_promo_card")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1.1f)
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "FINE JEWELLERY",
                            fontFamily = FontFamily.Default,
                            fontWeight = FontWeight.Bold,
                            fontSize = 9.sp,
                            letterSpacing = 1.5.sp,
                            color = LuxeGoldDark
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Radiant Solitaires & Chokers",
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = LuxeTextDark
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Explore Collection →",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = LuxeGold
                        )
                    }

                    Image(
                        painter = painterResource(id = R.drawable.jewellery_banner),
                        contentDescription = "Jewellery Banner",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .weight(0.9f)
                            .fillMaxHeight()
                    )
                }
            }
        }

        // 5. Watches Spotlight Banner
        item(span = { GridItemSpan(2) }) {
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = LuxeWhite),
                border = androidx.compose.foundation.BorderStroke(1.dp, LuxeGoldBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable { onNavigateTo(LuxeTab.WATCHES) }
                    .testTag("home_watches_promo_card")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.watches_banner),
                        contentDescription = "Watches Banner",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .weight(0.9f)
                            .fillMaxHeight()
                    )

                    Column(
                        modifier = Modifier
                            .weight(1.1f)
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "LUXURY TIMEPIECES",
                            fontFamily = FontFamily.Default,
                            fontWeight = FontWeight.Bold,
                            fontSize = 9.sp,
                            letterSpacing = 1.5.sp,
                            color = LuxeGoldDark
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Precision In Every Second",
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = LuxeTextDark
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Women • Men • Unisex →",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = LuxeGold
                        )
                    }
                }
            }
        }

        // 6. Why LuxeAura Section
        item(span = { GridItemSpan(2) }) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 18.dp, bottom = 8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .width(4.dp)
                            .height(20.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(LuxeGold)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Why LuxeAura",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = LuxeTextDark
                    )
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    WhyCard(
                        icon = Icons.Outlined.Diamond,
                        title = "18K Gold Finish & Certified Stones",
                        desc = "Finely polished with anti-tarnish coating and radiant lab-certified stones."
                    )
                    WhyCard(
                        icon = Icons.Outlined.AutoAwesome,
                        title = "Exclusive Handcrafted Elegance",
                        desc = "Carefully curated silhouettes combining royal heritage with minimalist beauty."
                    )
                    WhyCard(
                        icon = Icons.Outlined.LocalShipping,
                        title = "Insured Delivery & Cash On Delivery",
                        desc = "Fast, secure nationwide delivery across all major cities of Pakistan."
                    )
                    WhyCard(
                        icon = Icons.Outlined.SupportAgent,
                        title = "VIP WhatsApp Concierge",
                        desc = "Direct support on 03340634595 for bespoke sizing and gifting inquiries."
                    )
                }
            }
        }

        // 7. Footer
        item(span = { GridItemSpan(2) }) {
            LuxeFooter(
                onNavigateTo = onNavigateTo,
                onOpenAdminLogin = onOpenAdminLogin,
                modifier = Modifier.padding(top = 12.dp)
            )
        }
    }
}

@Composable
private fun WhyCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    desc: String
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = LuxeOffWhite),
        border = androidx.compose.foundation.BorderStroke(1.dp, LuxeGoldBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(LuxeGoldLight.copy(alpha = 0.35f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = LuxeGoldDark,
                    modifier = Modifier.size(18.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = LuxeTextDark
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = desc,
                    fontSize = 11.sp,
                    lineHeight = 15.sp,
                    color = LuxeTextMuted
                )
            }
        }
    }
}
