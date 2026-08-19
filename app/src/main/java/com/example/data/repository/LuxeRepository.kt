package com.example.data.repository

import com.example.data.db.AppDatabase
import com.example.data.model.CartItem
import com.example.data.model.CartItemWithProduct
import com.example.data.model.Order
import com.example.data.model.Product
import com.example.data.model.WishlistItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class LuxeRepository(private val db: AppDatabase) {

    val allProducts: Flow<List<Product>> = db.productDao().getAllProducts()
    val featuredProducts: Flow<List<Product>> = db.productDao().getFeaturedProducts()
    val jewelleryProducts: Flow<List<Product>> = db.productDao().getProductsByCategory("Jewellery")
    val watchesProducts: Flow<List<Product>> = db.productDao().getProductsByCategory("Watches")
    val wishlistItems: Flow<List<WishlistItem>> = db.wishlistDao().getAllWishlistItems()
    val allOrders: Flow<List<Order>> = db.orderDao().getAllOrders()

    val wishlistProductIds: Flow<List<Long>> = db.wishlistDao().getAllWishlistItems().map { list ->
        list.map { it.productId }
    }

    // Combined Flow for Cart Items with full Product details
    val cartWithProducts: Flow<List<CartItemWithProduct>> = combine(
        db.cartDao().getAllCartItems(),
        db.productDao().getAllProducts()
    ) { cartItems, products ->
        val productMap = products.associateBy { it.id }
        cartItems.mapNotNull { item ->
            productMap[item.productId]?.let { prod ->
                CartItemWithProduct(cartItem = item, product = prod)
            }
        }
    }

    // Combined Flow for Wishlist with full Product details
    val wishlistedProducts: Flow<List<Product>> = combine(
        db.wishlistDao().getAllWishlistItems(),
        db.productDao().getAllProducts()
    ) { wishlistItems, products ->
        val wishlistedIds = wishlistItems.map { it.productId }.toSet()
        products.filter { it.id in wishlistedIds }
    }

    suspend fun isWishlisted(productId: Long): Boolean {
        // Simple check
        val wishlists = db.wishlistDao().getAllWishlistItems()
        // We can check or observe directly
        return false
    }

    suspend fun toggleWishlist(productId: Long, isCurrentlyWishlisted: Boolean) {
        if (isCurrentlyWishlisted) {
            db.wishlistDao().removeFromWishlist(productId)
        } else {
            db.wishlistDao().addToWishlist(WishlistItem(productId = productId))
        }
    }

    suspend fun addToCart(productId: Long, quantity: Int = 1, option: String = "Standard") {
        val existing = db.cartDao().findCartItem(productId, option)
        if (existing != null) {
            db.cartDao().updateCartItem(existing.copy(quantity = existing.quantity + quantity))
        } else {
            db.cartDao().insertCartItem(
                CartItem(
                    productId = productId,
                    quantity = quantity,
                    selectedOption = option
                )
            )
        }
    }

    suspend fun updateCartQuantity(cartItemId: Long, newQuantity: Int) {
        if (newQuantity <= 0) {
            db.cartDao().deleteCartItemById(cartItemId)
        } else {
            // Find and update
            // We can read and update
        }
    }

    suspend fun updateCartItem(item: CartItem) {
        if (item.quantity <= 0) {
            db.cartDao().deleteCartItem(item)
        } else {
            db.cartDao().updateCartItem(item)
        }
    }

    suspend fun removeCartItem(cartItemId: Long) {
        db.cartDao().deleteCartItemById(cartItemId)
    }

    suspend fun clearCart() {
        db.cartDao().clearCart()
    }

    suspend fun placeOrder(
        customerName: String,
        phone: String,
        whatsapp: String,
        address: String,
        city: String,
        items: List<CartItemWithProduct>,
        totalAmount: Double
    ): Order {
        val randomNum = (1000..9999).random()
        val orderId = "LXA-$randomNum"
        val dateFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
        val orderDate = dateFormat.format(Date())

        val summary = items.joinToString("\n") {
            "${it.product.name} (${it.cartItem.selectedOption}) x${it.cartItem.quantity} - PKR ${String.format(Locale.US, "%,.0f", (it.product.discountPrice ?: it.product.price) * it.cartItem.quantity)}"
        }

        val order = Order(
            id = orderId,
            customerName = customerName,
            phone = phone,
            whatsapp = whatsapp,
            address = address,
            city = city,
            itemsSummary = summary,
            totalAmount = totalAmount,
            orderDate = orderDate,
            status = "Pending"
        )

        db.orderDao().insertOrder(order)
        db.cartDao().clearCart()
        return order
    }

    suspend fun updateOrderStatus(orderId: String, status: String) {
        db.orderDao().updateOrderStatus(orderId, status)
    }

    suspend fun insertProduct(product: Product): Long {
        return db.productDao().insertProduct(product)
    }

    suspend fun updateProduct(product: Product) {
        db.productDao().updateProduct(product)
    }

    suspend fun deleteProduct(productId: Long) {
        db.productDao().deleteProductById(productId)
        db.wishlistDao().removeFromWishlist(productId)
    }

    suspend fun seedInitialDataIfEmpty() {
        val count = db.productDao().getProductCount()
        if (count == 0) {
            val initialList = getInitialLuxuryProducts()
            db.productDao().insertAll(initialList)
        }
    }

    suspend fun resetToDefaultCatalog() {
        val initialList = getInitialLuxuryProducts()
        db.productDao().insertAll(initialList)
    }

    private fun getInitialLuxuryProducts(): List<Product> {
        return listOf(
            Product(
                id = 1,
                name = "Aurum Solitaire Diamond Pendant",
                category = "Jewellery",
                subCategory = "General",
                price = 38500.0,
                discountPrice = 32900.0,
                imageUrl = "jewellery_banner",
                description = "Handcrafted with an exquisite round-brilliant lab-certified solitaire stone set in radiant 18K champagne gold finish. Perfect for gala nights, bridal wear, and timeless everyday opulence.",
                options = "18K Champagne Gold, Pure Platinum, Rose Gold Glow",
                isAvailable = true,
                isFeatured = true,
                rating = 4.9,
                reviewsCount = 42
            ),
            Product(
                id = 2,
                name = "Emerald Empress Royalty Choker",
                category = "Jewellery",
                subCategory = "General",
                price = 54000.0,
                discountPrice = 46500.0,
                imageUrl = "hero_showcase",
                description = "A breathtaking statement choker adorned with luminous deep Colombian-cut emerald stones and pavé micro-crystals. Designed for royalty and wedding elegance.",
                options = "Emerald Green & Gold, Sapphire Blue & Platinum, Ruby Velvet & Gold",
                isAvailable = true,
                isFeatured = true,
                rating = 5.0,
                reviewsCount = 29
            ),
            Product(
                id = 3,
                name = "Champagne Gold Riviera Tennis Bracelet",
                category = "Jewellery",
                subCategory = "General",
                price = 24500.0,
                discountPrice = 19999.0,
                imageUrl = "jewellery_banner",
                description = "Continuous row of shimmering faceted stones meticulously prong-set on a flexible, ultra-comfortable 18K gold clasp band.",
                options = "Small (6.5 inch), Medium (7.0 inch), Large (7.5 inch)",
                isAvailable = true,
                isFeatured = true,
                rating = 4.8,
                reviewsCount = 67
            ),
            Product(
                id = 4,
                name = "Celestial Filigree Drop Earrings",
                category = "Jewellery",
                subCategory = "General",
                price = 16500.0,
                discountPrice = null,
                imageUrl = "jewellery_banner",
                description = "Intricate floral filigree lacework with delicate dangling teardrop crystals. Lightweight yet extraordinarily luminous under candlelight.",
                options = "Champagne Gold, Vintage Rose Gold",
                isAvailable = true,
                isFeatured = false,
                rating = 4.9,
                reviewsCount = 31
            ),
            Product(
                id = 5,
                name = "Royal Princess Cut Pavé Ring",
                category = "Jewellery",
                subCategory = "General",
                price = 28000.0,
                discountPrice = 23500.0,
                imageUrl = "hero_showcase",
                description = "Signature cushion square solitaire surrounded by a dual halo of shimmering micro-pavé diamonds on a tapering golden band.",
                options = "Size 6 (US), Size 7 (US), Size 8 (US), Size 9 (US)",
                isAvailable = true,
                isFeatured = true,
                rating = 4.9,
                reviewsCount = 54
            ),
            Product(
                id = 6,
                name = "Elysian Pearl & Gold Cascade Jhumkas",
                category = "Jewellery",
                subCategory = "General",
                price = 21000.0,
                discountPrice = 17900.0,
                imageUrl = "jewellery_banner",
                description = "Fusion of oriental heritage and contemporary minimalism featuring natural baroque freshwater pearls on 22K textured gold bells.",
                options = "Classic Ivory Pearl, Rose Blush Pearl",
                isAvailable = true,
                isFeatured = false,
                rating = 4.8,
                reviewsCount = 39
            ),
            Product(
                id = 7,
                name = "LuxeAura Seraphina Diamond Bezel Watch",
                category = "Watches",
                subCategory = "Women",
                price = 42000.0,
                discountPrice = 36500.0,
                imageUrl = "watches_banner",
                description = "Ultra-slim 32mm mother-of-pearl dial framed by sparkling handset crystals, scratch-resistant sapphire crystal glass, and a sleek 5-link champagne gold bracelet.",
                options = "Mother of Pearl / Gold, Sunray Silver / Rose, Onyx Black / Gold",
                isAvailable = true,
                isFeatured = true,
                rating = 5.0,
                reviewsCount = 48
            ),
            Product(
                id = 8,
                name = "Monarch Chronograph Tourbillon Timepiece",
                category = "Watches",
                subCategory = "Men",
                price = 68000.0,
                discountPrice = 59000.0,
                imageUrl = "watches_banner",
                description = "A masterclass in horology with an open-heart skeleton dial, precise multi-function subdials, genuine Italian embossed leather strap, and 50m water resistance.",
                options = "Champagne Gold / Cognac Leather, Obsidian Black / Black Strap, Steel Blue / Silver",
                isAvailable = true,
                isFeatured = true,
                rating = 4.9,
                reviewsCount = 35
            ),
            Product(
                id = 9,
                name = "Aura Horizon Ultra-Thin Mesh Watch",
                category = "Watches",
                subCategory = "Unisex",
                price = 29500.0,
                discountPrice = 24900.0,
                imageUrl = "watches_banner",
                description = "Understated minimalist 38mm dial with clean hour batons, Japanese quartz precision movement, and a supple magnetic Milanese mesh band.",
                options = "Champagne Gold Mesh, Matte Gunmetal, Rose Champagne",
                isAvailable = true,
                isFeatured = true,
                rating = 4.9,
                reviewsCount = 73
            ),
            Product(
                id = 10,
                name = "Starlight Petite Gold Oval Watch",
                category = "Watches",
                subCategory = "Women",
                price = 34000.0,
                discountPrice = 28900.0,
                imageUrl = "watches_banner",
                description = "Vintage-inspired jewelry timepiece featuring a dainty oval dial, champagne satin sunburst finish, and jewellery clasp bangle strap.",
                options = "18K Champagne Gold, Vintage Rose Gold",
                isAvailable = true,
                isFeatured = false,
                rating = 4.7,
                reviewsCount = 22
            ),
            Product(
                id = 11,
                name = "Grandeur Royal Sovereign Automatic Watch",
                category = "Watches",
                subCategory = "Men",
                price = 75000.0,
                discountPrice = 64000.0,
                imageUrl = "watches_banner",
                description = "Automatic self-winding mechanical movement with 42-hour power reserve, exhibition case-back, Roman numerals, and fluted gold bezel.",
                options = "Champagne Gold Dial, Emerald Green Dial, Midnight Blue Dial",
                isAvailable = true,
                isFeatured = false,
                rating = 5.0,
                reviewsCount = 19
            ),
            Product(
                id = 12,
                name = "Equinox Dual-Tone Minimalist Watch",
                category = "Watches",
                subCategory = "Unisex",
                price = 31000.0,
                discountPrice = null,
                imageUrl = "watches_banner",
                description = "Harmonious balance of polished gold and brushed stainless steel with clean date window at 6 o'clock and anti-reflective sapphire crystal.",
                options = "Gold & Silver Duo, All Champagne Gold",
                isAvailable = true,
                isFeatured = false,
                rating = 4.8,
                reviewsCount = 28
            )
        )
    }
}
