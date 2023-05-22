package fr.kotmine.persistanceapi.models.shop

data class ItemDescriptionBean(
    var itemId: Int = 0,
    var itemName: String? = null,
    var itemDesc: String? = null,
    var priceCoins: Int = 42424242,
    var priceStars: Int = 42424242,
    var gameCategory: Int = 0,
    var itemMinecraftId: String? = null,
    var itemRarity: String? = null,
    var rankAccessibility: String? = null
)
