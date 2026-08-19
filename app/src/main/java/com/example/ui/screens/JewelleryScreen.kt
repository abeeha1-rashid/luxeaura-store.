package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
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
fun JewelleryScreen(
    jewelleryProducts: List<Product>,
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
        contentPadding = PaddingValues(bottom = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
            .fillMaxSize()
            .background(LuxeWhite)
            .testTag("jewellery_screen")
    ) {
        // Banner Header
        item(span = { GridItemSpan(2) }) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, LuxeGoldBorder, RoundedCornerShape(16.dp))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.jewellery_banner),
                    contentDescription = "Fine Jewellery",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color(0x991E1A17),
                                    Color(0xDD1E1A17)
                                )
                            )
                        )
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "LUXURY JEWELLERY",
                        fontFamily = FontFamily.Default,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        letterSpacing = 2.sp,
                        color = LuxeGoldLight
                    )
                    Text(
                        text = "The Bridal & Solitaire Collection",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = LuxeWhite,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "18K Champagne Gold • Diamonds • Natural Baroque Pearls",
                        fontSize = 11.sp,
                        color = LuxeWhite.copy(alpha = 0.85f),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        // Count header
        item(span = { GridItemSpan(2) }) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Showing ${jewelleryProducts.size} Designs",
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                    color = LuxeTextMuted
                )
                Text(
                    text = "Free Insured Shipping",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = LuxeGoldDark
                )
            }
        }

        // Products Grid
        if (jewelleryProducts.isEmpty()) {
            item(span = { GridItemSpan(2) }) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No jewellery pieces found matching your criteria.",
                        fontSize = 13.sp,
                        color = LuxeTextMuted,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            items(
                items = jewelleryProducts,
                key = { it.id }
            ) { product ->
                Box(modifier = Modifier.padding(horizontal = 8.dp)) {
                    ProductCard(
                        product = product,
                        isWishlisted = isWishlisted(product.id),
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
