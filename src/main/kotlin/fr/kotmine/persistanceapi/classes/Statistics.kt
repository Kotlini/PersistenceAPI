package fr.kotmine.persistanceapi.classes

import fr.kotmine.persistanceapi.managers.statistics.FallenKingDomStatisticsManager
import fr.kotmine.persistanceapi.managers.statistics.JukeBoxStatisticsManager
import fr.kotmine.persistanceapi.managers.statistics.VersusStatisticsManager
import fr.kotmine.persistanceapi.models.players.PlayerBean
import fr.kotmine.persistanceapi.models.statistics.PlayerStatisticsBean
import javax.sql.DataSource

object Statistics {
    @Throws(Exception::class)
    fun getAllPlayerStatistics(player: PlayerBean, dataSource: DataSource): PlayerStatisticsBean {
        // Create the aggregation of different statistics bean
        return PlayerStatisticsBean(JukeBoxStatisticsManager.getJukeBoxStatistics(player, dataSource)!!,
            FallenKingDomStatisticsManager.getFallenKingDomStatistics(player, dataSource)!!,
            VersusStatisticsManager.getVersusStatistics(player, dataSource)!!)
    }

    @Throws(Exception::class)
    fun updateAllPlayerStatistics(player: PlayerBean, data: PlayerStatisticsBean, dataSource: DataSource) {
        // Update the different statistics bean
        try {
            JukeBoxStatisticsManager.updateJukeBoxStatistics(player, data.jukeBoxStatsBean!!, dataSource)
            FallenKingDomStatisticsManager.updateFallenKingDomStatistics(player, data.fallenKingdomStatisticsBean, dataSource)
            VersusStatisticsManager.updateVersusStatistics(player, data.versusStatisticsBean, dataSource)
        } catch (exception: Exception) {
            exception.printStackTrace()
            throw exception
        }
    }

}