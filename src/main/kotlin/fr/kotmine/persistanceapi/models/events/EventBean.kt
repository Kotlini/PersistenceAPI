package fr.kotmine.persistanceapi.models.events

import java.sql.Timestamp
import java.util.UUID

data class EventBean(
    val eventId: Long,
    var eventOrganizer: UUID,
    var eventTemplate: String,
    var rewardCoins: Int,
    var rewardPearls: Int,
    var eventDate: Timestamp
)
