package fr.kotmine.persistanceapi.models.shop

import java.sql.Timestamp

data class PromotionsBean(
    var promotionId: Long = 0,
    var promotionType: Int = 0,
    var game: Int = 0,
    var multiplier: Int = 0,
    var message: String? = null,
    var startDate: Timestamp? = null,
    var endDate: Timestamp? = null
)

