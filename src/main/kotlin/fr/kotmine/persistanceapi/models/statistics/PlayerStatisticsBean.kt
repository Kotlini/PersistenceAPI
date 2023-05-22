package fr.kotmine.persistanceapi.models.statistics

data class PlayerStatisticsBean(
    var jukeBoxStatsBean: JukeBoxStatisticsBean?,
    var fallenKingdomStatisticsBean: FallenKingdomStatisticsBean,
    var versusStatisticsBean: VersusStatisticsBean
)

