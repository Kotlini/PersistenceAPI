package fr.kotmine.persistanceapi.models.statistics

import java.util.*

data class HostStatisticsBean(
    var id: Long = 0,
    var templateId: String = "",
    var hostId: String = "",
    var ipAddress: String = "",
    var playerUuid: UUID = UUID(0, 0),
    var startedTime: Long = 0,
    var playedTime: Long = 0
)

