package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material.icons.outlined.Sort
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.SortOption
import com.example.ui.theme.*
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchFilterSortBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    selectedCategory: String,
    onCategoryChange: (String) -> Unit,
    selectedWatchSubCategory: String,
    onWatchSubCategoryChange: (String) -> Unit,
    maxPrice: Float,
    onMaxPriceChange: (Float) -> Unit,
    inStockOnly: Boolean,
    onInStockOnlyChange: (Boolean) -> Unit,
    sortOption: SortOption,
    onSortOptionChange: (SortOption) -> Unit,
    modifier: Modifier = Modifier
) {
    var isFilterExpanded by remember { mutableStateOf(false) }
    var isSortMenuOpen by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(LuxeWhite)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Search Input Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                placeholder = {
                    Text(
                        text = "Search jewellery, watches, diamonds, rings...",
                        fontSize = 12.5.sp,
                        color = LuxeTextLight
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = LuxeGoldDark,
                        modifier = Modifier.size(18.dp)
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(
                            onClick = { onSearchQueryChange("") },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear",
                                tint = LuxeTextMuted,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = LuxeGold,
                    unfocusedBorderColor = LuxeGoldBorder,
                    focusedContainerColor = LuxeOffWhite,
                    unfocusedContainerColor = LuxeOffWhite
                ),
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .testTag("search_input_field")
            )

            // Filter Toggle Button
            OutlinedButton(
                onClick = { isFilterExpanded = !isFilterExpanded },
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (isFilterExpanded) LuxeGoldDark else LuxeGoldBorder
                ),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = if (isFilterExpanded) LuxeGoldGlow else LuxeOffWhite,
                    contentColor = if (isFilterExpanded) LuxeGoldDark else LuxeCharcoal
                ),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 8.dp),
                modifier = Modifier
                    .height(48.dp)
                    .testTag("filter_toggle_button")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.FilterList,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "Filters",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Sort Dropdown Button
            Box {
                OutlinedButton(
                    onClick = { isSortMenuOpen = true },
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, LuxeGoldBorder),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = LuxeOffWhite,
                        contentColor = LuxeCharcoal
                    ),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 8.dp),
                    modifier = Modifier
                        .height(48.dp)
                        .testTag("sort_dropdown_button")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Sort,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Sort",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                DropdownMenu(
                    expanded = isSortMenuOpen,
                    onDismissRequest = { isSortMenuOpen = false },
                    modifier = Modifier.background(LuxeWhite)
                ) {
                    SortOption.values().forEach { option ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = option.displayName,
                                    fontSize = 13.sp,
                                    fontWeight = if (sortOption == option) FontWeight.Bold else FontWeight.Normal,
                                    color = if (sortOption == option) LuxeGoldDark else LuxeTextDark
                                )
                            },
                            onClick = {
                                onSortOptionChange(option)
                                isSortMenuOpen = false
                            },
                            leadingIcon = {
                                if (sortOption == option) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = LuxeGold,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        )
                    }
                }
            }
        }

        // Quick Category Filter Pills Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            listOf("All", "Jewellery", "Watches").forEach { cat ->
                val isSelected = selectedCategory.equals(cat, ignoreCase = true)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (isSelected) LuxeGoldDark else LuxeOffWhite)
                        .border(
                            1.dp,
                            if (isSelected) LuxeGoldDark else LuxeGoldBorder,
                            RoundedCornerShape(16.dp)
                        )
                        .clickable { onCategoryChange(cat) }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = cat,
                        fontSize = 11.5.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) LuxeWhite else LuxeTextDark
                    )
                }
            }

            // If Watches or All is active, show Watch Subcategories
            if (selectedCategory.equals("Watches", ignoreCase = true) || selectedCategory.equals("All", ignoreCase = true)) {
                Box(
                    modifier = Modifier
                        .height(18.dp)
                        .width(1.dp)
                        .background(LuxeGoldBorder)
                )

                listOf("All Watches", "Women", "Men", "Unisex").forEach { sub ->
                    val actualSub = if (sub == "All Watches") "All" else sub
                    val isSelected = selectedWatchSubCategory.equals(actualSub, ignoreCase = true)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (isSelected) LuxeGoldLight else LuxeWhite)
                            .border(
                                1.dp,
                                if (isSelected) LuxeGold else LuxeGoldBorder.copy(alpha = 0.5f),
                                RoundedCornerShape(16.dp)
                            )
                            .clickable { onWatchSubCategoryChange(actualSub) }
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = sub,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                            color = if (isSelected) LuxeCharcoal else LuxeTextMuted
                        )
                    }
                }
            }
        }

        // Expanded Advanced Filters Drawer
        AnimatedVisibility(visible = isFilterExpanded) {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = LuxeOffWhite),
                border = androidx.compose.foundation.BorderStroke(1.dp, LuxeGoldBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Price Range Slider
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Price Range",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = LuxeCharcoal
                            )
                            Text(
                                text = "Up to PKR ${String.format(Locale.US, "%,.0f", maxPrice)}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = LuxeGoldDark
                            )
                        }

                        Slider(
                            value = maxPrice,
                            onValueChange = onMaxPriceChange,
                            valueRange = 10000f..100000f,
                            steps = 9,
                            colors = SliderDefaults.colors(
                                thumbColor = LuxeGold,
                                activeTrackColor = LuxeGold,
                                inactiveTrackColor = LuxeGoldBorder
                            )
                        )
                    }

                    // In-Stock Only Switch
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Show In-Stock Products Only",
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Medium,
                            color = LuxeCharcoal
                        )
                        Switch(
                            checked = inStockOnly,
                            onCheckedChange = onInStockOnlyChange,
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = LuxeWhite,
                                checkedTrackColor = LuxeGold,
                                uncheckedThumbColor = LuxeTextMuted,
                                uncheckedTrackColor = LuxeCream
                            )
                        )
                    }

                    // Reset Filters button
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(
                            onClick = {
                                onCategoryChange("All")
                                onWatchSubCategoryChange("All")
                                onMaxPriceChange(100000f)
                                onInStockOnlyChange(false)
                                onSearchQueryChange("")
                                onSortOptionChange(SortOption.NEWEST)
                            }
                        ) {
                            Text(
                                text = "Reset Filters",
                                fontSize = 11.5.sp,
                                color = LuxeGoldDark,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}
