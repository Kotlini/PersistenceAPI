package fr.kotmine.persistanceapi.models.statistics

import java.sql.Timestamp
import java.util.*

data class FallenKingdomStatisticsBean(
    var uuid: UUID = UUID(0, 0),
    var deaths: Int = 0,
    var kills: Int = 0,
    var heartKills: Int = 0,
    var playedGames: Int = 0,
    var wins: Int = 0,
    var creationDate: Timestamp = Timestamp(0),
    var startedTime: Timestamp = Timestamp(0),
    var playedTime: Long = 0
)
