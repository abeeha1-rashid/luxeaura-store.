package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.R
import com.example.data.model.Product
import com.example.ui.theme.*
import java.util.Locale

@Composable
fun ProductCard(
    product: Product,
    isWishlisted: Boolean,
    onWishlistToggle: (Product) -> Unit,
    onAddToCart: (Product) -> Unit,
    onViewDetails: (Product) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val hasDiscount = product.discountPrice != null && product.discountPrice < product.price
    val discountPercent = if (hasDiscount) {
        (((product.price - product.discountPrice!!) / product.price) * 100).toInt()
    } else 0

    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = LuxeWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp),
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = LuxeGoldBorder,
                shape = RoundedCornerShape(24.dp)
            )
            .clickable { onViewDetails(product) }
            .testTag("product_card_${product.id}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            // Editorial Image Container (rounded-2xl with #FAF9F6 background)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(136.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(LuxeOffWhite)
            ) {
                // Determine drawable resource or URL
                val drawableRes = when (product.imageUrl) {
                    "jewellery_banner" -> R.drawable.jewellery_banner
                    "watches_banner" -> R.drawable.watches_banner
                    "hero_showcase" -> R.drawable.hero_showcase
                    "luxeaura_logo" -> R.drawable.luxeaura_logo
                    else -> null
                }

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    if (drawableRes != null) {
                        Image(
                            painter = painterResource(id = drawableRes),
                            contentDescription = product.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else if (product.imageUrl.startsWith("http")) {
                        AsyncImage(
                            model = ImageRequest.Builder(context)
                                .data(product.imageUrl)
                                .crossfade(true)
                                .build(),
                            placeholder = painterResource(id = R.drawable.luxeaura_logo),
                            error = painterResource(id = R.drawable.jewellery_banner),
                            contentDescription = product.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Image(
                            painter = painterResource(
                                id = if (product.category == "Watches") R.drawable.watches_banner else R.drawable.jewellery_banner
                            ),
                            contentDescription = product.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }

                // Discount Badge (Top Left)
                if (hasDiscount) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(8.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(LuxeGold)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "-$discountPercent%",
                            color = LuxeWhite,
                            fontSize = 8.5.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else if (!product.isAvailable) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(8.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(LuxeCharcoal.copy(alpha = 0.85f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "Sold Out",
                            color = LuxeWhite,
                            fontSize = 8.5.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Floating Wishlist Heart Button (Top Right)
                IconButton(
                    onClick = { onWishlistToggle(product) },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(6.dp)
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(LuxeWhite.copy(alpha = 0.85f))
                        .shadow(1.dp, CircleShape)
                        .testTag("wishlist_toggle_${product.id}")
                ) {
                    Icon(
                        imageVector = if (isWishlisted) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Wishlist",
                        tint = if (isWishlisted) LuxeGold else LuxeTextMuted,
                        modifier = Modifier.size(15.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Product Name
            Text(
                text = product.name,
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Bold,
                fontSize = 12.5.sp,
                lineHeight = 16.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = LuxeTextDark
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Pricing (Rs. X,XXX in Editorial Serif Gold)
            val displayPrice = product.discountPrice ?: product.price
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Rs. ${String.format(Locale.US, "%,.0f", displayPrice)}",
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = LuxeGold
                )

                if (hasDiscount) {
                    Text(
                        text = "Rs. ${String.format(Locale.US, "%,.0f", product.price)}",
                        fontFamily = FontFamily.Default,
                        fontWeight = FontWeight.Normal,
                        fontSize = 10.5.sp,
                        color = LuxeTextLight,
                        textDecoration = TextDecoration.LineThrough
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Editorial "Add to Cart" Button (bg-neutral-900 text-white rounded-xl text-[10px] font-bold uppercase tracking-wider)
            Button(
                onClick = { onAddToCart(product) },
                enabled = product.isAvailable,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = LuxeCharcoal,
                    contentColor = LuxeWhite,
                    disabledContainerColor = LuxeTextLight.copy(alpha = 0.3f)
                ),
                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 6.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(34.dp)
                    .testTag("add_to_cart_${product.id}")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingBag,
                        contentDescription = null,
                        modifier = Modifier.size(12.dp),
                        tint = LuxeGoldLight
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = if (product.isAvailable) "Add to Cart" else "Out of Stock",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp
                    )
                }
            }
        }
    }
}
