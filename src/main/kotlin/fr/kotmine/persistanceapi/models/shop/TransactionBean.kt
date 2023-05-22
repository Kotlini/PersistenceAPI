package fr.kotmine.persistanceapi.models.shop

import java.sql.Timestamp
import java.util.*

data class TransactionBean(
    var transactionId: Long = 0,
    var itemId: Int? = null,
    var priceCoins: Int? = null,
    var priceStars: Int? = null,
    var transactionDate: Timestamp? = null,
    var selected: Boolean = false,
    var uuidBuyer: UUID? = null
)

