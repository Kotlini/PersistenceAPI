package fr.kotmine.persistanceapi.models.statistics

import java.sql.Timestamp
import java.util.*

data class NetworkStatisticsBean(
    var uuid: UUID,
    var playedTime: Long,
    var creationDate: Timestamp,
    var updateDate: Timestamp
)

