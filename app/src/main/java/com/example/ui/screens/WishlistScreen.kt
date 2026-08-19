package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Product
import com.example.ui.LuxeTab
import com.example.ui.components.LuxeFooter
import com.example.ui.components.ProductCard
import com.example.ui.theme.*

@Composable
fun WishlistScreen(
    wishlistedProducts: List<Product>,
    onWishlistToggle: (Product) -> Unit,
    onAddToCart: (Product) -> Unit,
    onViewDetails: (Product) -> Unit,
    onNavigateTo: (LuxeTab) -> Unit,
    onOpenAdminLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(bottom = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
            .fillMaxSize()
            .background(LuxeWhite)
            .testTag("wishlist_screen")
    ) {
        // Header
        item(span = { GridItemSpan(2) }) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 8.dp, start = 16.dp, end = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "YOUR SAVED PIECES",
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    letterSpacing = 2.sp,
                    color = LuxeGoldDark
                )
                Text(
                    text = "Wishlist ❤️",
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = LuxeCharcoal
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${wishlistedProducts.size} luxury items saved for later",
                    fontSize = 12.sp,
                    color = LuxeTextMuted
                )
            }
        }

        // Empty State or List
        if (wishlistedProducts.isEmpty()) {
            item(span = { GridItemSpan(2) }) {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = LuxeOffWhite),
                    border = androidx.compose.foundation.BorderStroke(1.dp, LuxeGoldBorder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                        .testTag("empty_wishlist_card")
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(CircleShape)
                                .background(LuxeGoldGlow)
                                .border(1.dp, LuxeGoldBorder, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.FavoriteBorder,
                                contentDescription = null,
                                tint = LuxeGoldDark,
                                modifier = Modifier.size(36.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Your Wishlist is Empty",
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = LuxeCharcoal
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Explore our exquisite collection of fine jewellery and luxury timepieces to save your favorite designs.",
                            fontSize = 12.5.sp,
                            lineHeight = 18.sp,
                            color = LuxeTextMuted,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Button(
                                onClick = { onNavigateTo(LuxeTab.JEWELLERY) },
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = LuxeGold,
                                    contentColor = LuxeWhite
                                )
                            ) {
                                Text("Shop Jewellery", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }

                            OutlinedButton(
                                onClick = { onNavigateTo(LuxeTab.WATCHES) },
                                shape = RoundedCornerShape(10.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, LuxeGold)
                            ) {
                                Text("Explore Watches", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = LuxeGoldDark)
                            }
                        }
                    }
                }
            }
        } else {
            items(
                items = wishlistedProducts,
                key = { it.id }
            ) { product ->
                Box(modifier = Modifier.padding(horizontal = 8.dp)) {
                    ProductCard(
                        product = product,
                        isWishlisted = true,
                        onWishlistToggle = onWishlistToggle,
                        onAddToCart = onAddToCart,
                        onViewDetails = onViewDetails
                    )
                }
            }
        }

        // Footer
        item(span = { GridItemSpan(2) }) {
            LuxeFooter(
                onNavigateTo = onNavigateTo,
                onOpenAdminLogin = onOpenAdminLogin,
                modifier = Modifier.padding(top = 24.dp)
            )
        }
    }
}
