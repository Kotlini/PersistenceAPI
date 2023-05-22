package fr.kotmine.persistanceapi.models.statistics

import java.sql.Timestamp
import java.util.*

data class JukeBoxStatisticsBean(
    var uuid: UUID = UUID(0, 0),
    var mehs: Int = 0,
    var woots: Int = 0,
    var wootsGiven: Int = 0,
    var creationDate: Timestamp = Timestamp(0),
    var updateDate: Timestamp = Timestamp(0),
    var playedTime: Long = 0
)

