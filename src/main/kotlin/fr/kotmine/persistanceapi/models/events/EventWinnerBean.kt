package fr.kotmine.persistanceapi.models.events

import java.util.UUID

data class EventWinnerBean(
    val winId: Long,
    var eventId: Long,
    var eventWinner: UUID
)