package fr.kotmine.persistanceapi.managers.statistics

import fr.kotmine.persistanceapi.models.players.PlayerBean
import fr.kotmine.persistanceapi.models.statistics.LeaderboardBean
import fr.kotmine.persistanceapi.models.statistics.NetworkStatisticsBean
import fr.kotmine.persistanceapi.utils.Transcoder
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Timestamp
import java.util.*
import javax.sql.DataSource

object NetworkStatisticsManager {
    private var connection: Connection? = null
    private var statement: PreparedStatement? = null
    private var resultSet: ResultSet? = null

    @Throws(Exception::class)
    fun getNetworkStatistics(player: PlayerBean, dataSource: DataSource): NetworkStatisticsBean? {
        val networkStats: NetworkStatisticsBean?
        try {
            connection = dataSource.connection
            val sql = "select HEX(uuid) as uuid, creation_date, update_date, played_time from network_stats where uuid = UNHEX(?)"
            statement = connection?.prepareStatement(sql)
            statement?.setString(1, Transcoder.encode(player.uuid.toString()))
            resultSet = statement?.executeQuery()
            if (resultSet?.next() == true) {
                val playerUuid = Transcoder.decode(resultSet?.getString("uuid")!!)
                val uuid = UUID.fromString(playerUuid)
                val creationDate = resultSet?.getTimestamp("creation_date")
                val updateDate = resultSet?.getTimestamp("update_date")
                val playedTime = resultSet?.getLong("played_time")
                networkStats = NetworkStatisticsBean(uuid, playedTime!!, creationDate!!, updateDate!!)
            } else {
                close()
                createEmptyNetworkStatistics(player, dataSource)
                close()
                val newNetworkStats = getNetworkStatistics(player, dataSource)
                close()
                return newNetworkStats
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
            throw exception
        } finally {
            close()
        }
        return networkStats
    }

    @Throws(Exception::class)
    private fun createEmptyNetworkStatistics(player: PlayerBean, dataSource: DataSource) {
        try {
            val networkStats = NetworkStatisticsBean(player.uuid!!, 0, Timestamp(System.currentTimeMillis()), Timestamp(System.currentTimeMillis()))
            connection = dataSource.connection
            val sql = "insert into network_stats (uuid, creation_date, update_date, played_time) values (UNHEX(?), now(), now(), played_time = ?)"
            statement = connection?.prepareStatement(sql)
            statement?.setString(1, Transcoder.encode(player.uuid.toString()))
            statement?.setLong(2, networkStats.playedTime)
            statement?.executeUpdate()
        } catch (exception: Exception) {
            exception.printStackTrace()
            throw exception
        } finally {
            close()
        }
    }

    @Throws(Exception::class)
    fun updateNetworkStatistics(player: PlayerBean, networkStats: NetworkStatisticsBean, dataSource: DataSource) {
        try {
            if (getNetworkStatistics(player, dataSource) == null) {
                createEmptyNetworkStatistics(player, dataSource)
            } else {
                connection = dataSource.connection
                val sql = "update network_stats set played_time = ?, update_date = now() where uuid = UNHEX(?)"
                statement = connection?.prepareStatement(sql)
                statement?.setLong(1, networkStats.playedTime)
                statement?.setString(2, Transcoder.encode(player.uuid.toString()))
                statement?.executeUpdate()
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
            throw exception
        } finally {
            close()
        }
    }

    @Throws(Exception::class)
    fun getLeaderBoard(category: String, dataSource: DataSource): List<LeaderboardBean> {
        val leaderBoard: MutableList<LeaderboardBean> = ArrayList()
        try {
            connection = dataSource.connection
            val sql = String.format("select p.name as name, d.%1\$s as score from players as p, network_stats as d where p.uuid = d.uuid order by d.%2\$s desc limit 3", category, category)
            statement = connection?.prepareStatement(sql)
            resultSet = statement?.executeQuery()
            while (resultSet?.next() == true) {
                val bean = LeaderboardBean(resultSet?.getString("name")!!, resultSet?.getInt("score")!!)
                leaderBoard.add(bean)
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
            throw exception
        } finally {
            close()
        }
        return leaderBoard
    }

    @Throws(Exception::class)
    private fun close() {
        try {
            resultSet?.close()
            statement?.close()
            connection?.close()
        } catch (exception: Exception) {
            exception.printStackTrace()
            throw exception
        }
    }
}
