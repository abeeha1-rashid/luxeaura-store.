package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.LuxeTab
import com.example.ui.theme.*

@Composable
fun LuxeNavbar(
    activeTab: LuxeTab,
    wishlistCount: Int,
    cartCount: Int,
    isSearchOpen: Boolean,
    onTabSelected: (LuxeTab) -> Unit,
    onToggleSearch: () -> Unit,
    onOpenAdminLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = LuxeWhite,
        shadowElevation = 2.dp,
        modifier = modifier
            .fillMaxWidth()
            .border(width = 1.dp, color = LuxeGoldBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
        ) {
            // Top branding row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Editorial Logo & Name (Italic Serif LuxeAura.pk)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable { onTabSelected(LuxeTab.HOME) }
                        .testTag("nav_logo_button")
                ) {
                    Text(
                        text = "LuxeAura.pk",
                        fontFamily = FontFamily.Serif,
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        letterSpacing = 0.5.sp,
                        color = LuxeGold
                    )
                }

                // Quick Action Icons (Search, Wishlist, Cart, Discreet Admin)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Search Button
                    IconButton(
                        onClick = onToggleSearch,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .testTag("nav_search_button")
                    ) {
                        Icon(
                            imageVector = if (isSearchOpen) Icons.Default.Close else Icons.Outlined.Search,
                            contentDescription = "Search",
                            tint = if (isSearchOpen) LuxeGoldDark else LuxeTextDark,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    // Wishlist with Badge
                    IconButton(
                        onClick = { onTabSelected(LuxeTab.WISHLIST) },
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .testTag("nav_wishlist_button")
                    ) {
                        BadgedBox(
                            badge = {
                                if (wishlistCount > 0) {
                                    Badge(
                                        containerColor = LuxeGold,
                                        contentColor = LuxeWhite
                                    ) {
                                        Text(text = wishlistCount.toString(), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = if (activeTab == LuxeTab.WISHLIST) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                contentDescription = "Wishlist",
                                tint = if (activeTab == LuxeTab.WISHLIST) LuxeGold else LuxeTextDark,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    // Shopping Cart with Gold Badge (matches Editorial HTML bag icon with gold badge)
                    IconButton(
                        onClick = { onTabSelected(LuxeTab.CART) },
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .testTag("nav_cart_button")
                    ) {
                        BadgedBox(
                            badge = {
                                if (cartCount > 0) {
                                    Badge(
                                        containerColor = LuxeGold,
                                        contentColor = LuxeWhite
                                    ) {
                                        Text(text = cartCount.toString(), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = if (activeTab == LuxeTab.CART) Icons.Filled.ShoppingBag else Icons.Outlined.ShoppingBag,
                                contentDescription = "Shopping Cart",
                                tint = if (activeTab == LuxeTab.CART) LuxeGoldDark else LuxeTextDark,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    // Discreet Admin Portal Button
                    IconButton(
                        onClick = onOpenAdminLogin,
                        modifier = Modifier.size(30.dp).testTag("nav_admin_button")
                    ) {
                        Icon(
                            imageVector = if (activeTab == LuxeTab.ADMIN) Icons.Filled.AdminPanelSettings else Icons.Outlined.Lock,
                            contentDescription = "Admin Portal",
                            tint = if (activeTab == LuxeTab.ADMIN) LuxeGold else LuxeTextLight.copy(alpha = 0.5f),
                            modifier = Modifier.size(15.dp)
                        )
                    }
                }
            }

            // Scrollable Category Navigation Row (Editorial pill tabs)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                NavTabItem(
                    label = "HOME",
                    icon = Icons.Outlined.Home,
                    isSelected = activeTab == LuxeTab.HOME,
                    onClick = { onTabSelected(LuxeTab.HOME) },
                    testTag = "tab_home"
                )
                NavTabItem(
                    label = "JEWELLERY",
                    icon = Icons.Outlined.Diamond,
                    isSelected = activeTab == LuxeTab.JEWELLERY,
                    onClick = { onTabSelected(LuxeTab.JEWELLERY) },
                    testTag = "tab_jewellery"
                )
                NavTabItem(
                    label = "WATCHES",
                    icon = Icons.Outlined.Watch,
                    isSelected = activeTab == LuxeTab.WATCHES,
                    onClick = { onTabSelected(LuxeTab.WATCHES) },
                    testTag = "tab_watches"
                )
                NavTabItem(
                    label = "WISHLIST",
                    icon = Icons.Outlined.FavoriteBorder,
                    isSelected = activeTab == LuxeTab.WISHLIST,
                    badgeCount = wishlistCount,
                    onClick = { onTabSelected(LuxeTab.WISHLIST) },
                    testTag = "tab_wishlist"
                )
                NavTabItem(
                    label = "CONTACT",
                    icon = Icons.Outlined.Phone,
                    isSelected = activeTab == LuxeTab.CONTACT,
                    onClick = { onTabSelected(LuxeTab.CONTACT) },
                    testTag = "tab_contact"
                )
                NavTabItem(
                    label = "CART",
                    icon = Icons.Outlined.ShoppingBag,
                    isSelected = activeTab == LuxeTab.CART,
                    badgeCount = cartCount,
                    onClick = { onTabSelected(LuxeTab.CART) },
                    testTag = "tab_cart"
                )
            }

            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
private fun NavTabItem(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    badgeCount: Int = 0,
    onClick: () -> Unit,
    testTag: String
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(
                if (isSelected) LuxeGold.copy(alpha = 0.12f) else Color.Transparent
            )
            .border(
                width = 1.dp,
                color = if (isSelected) LuxeGold else Color.Transparent,
                shape = RoundedCornerShape(20.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 6.dp)
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected) LuxeGold else LuxeTextMuted,
                modifier = Modifier.size(13.dp)
            )
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                letterSpacing = 0.8.sp,
                color = if (isSelected) LuxeGold else LuxeTextMuted
            )
            if (badgeCount > 0 && !isSelected) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .clip(CircleShape)
                        .background(LuxeGoldLight),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = badgeCount.toString(),
                        fontSize = 8.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = LuxeGoldDark
                    )
                }
            }
        }
    }
}
