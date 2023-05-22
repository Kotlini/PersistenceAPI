package fr.kotmine.persistanceapi.managers.statistics

import fr.kotmine.persistanceapi.models.players.PlayerBean
import fr.kotmine.persistanceapi.models.statistics.JukeBoxStatisticsBean
import fr.kotmine.persistanceapi.models.statistics.LeaderboardBean
import fr.kotmine.persistanceapi.utils.Transcoder
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Timestamp
import java.util.ArrayList
import java.util.UUID
import javax.sql.DataSource

object JukeBoxStatisticsManager {
    private var connection: Connection? = null
    private var statement: PreparedStatement? = null
    private var resultSet: ResultSet? = null

    @Throws(Exception::class)
    fun getJukeBoxStatistics(player: PlayerBean, dataSource: DataSource): JukeBoxStatisticsBean? {
        var jukeBoxStats: JukeBoxStatisticsBean? = null

        try {
            connection = dataSource.connection
            val sql = "select HEX(uuid) as uuid, mehs, woots, woots_given, creation_date, update_date, played_time from jukebox_stats where uuid = UNHEX(?)"
            statement = connection?.prepareStatement(sql)
            statement?.setString(1, Transcoder.encode(player.uuid.toString()))
            resultSet = statement?.executeQuery()

            if (resultSet?.next() == true) {
                val playerUuid = Transcoder.decode(resultSet?.getString("uuid")!!)
                val uuid = UUID.fromString(playerUuid)
                val mehs = resultSet?.getInt("mehs")
                val woots = resultSet?.getInt("woots")
                val wootsGiven = resultSet?.getInt("woots_given")
                val creationDate = resultSet?.getTimestamp("creation_date")
                val updateDate = resultSet?.getTimestamp("update_date")
                val playedTime = resultSet?.getLong("played_time")

                jukeBoxStats = JukeBoxStatisticsBean(uuid, mehs!!, woots!!, wootsGiven!!, creationDate!!, updateDate!!, playedTime!!)
            } else {
                close()
                createEmptyJukeBoxStatistics(player, dataSource)
                close()

                val newJukeBoxStats = getJukeBoxStatistics(player, dataSource)
                close()

                return newJukeBoxStats
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
            throw exception
        } finally {
            close()
        }
        return jukeBoxStats
    }

    @Throws(Exception::class)
    private fun createEmptyJukeBoxStatistics(player: PlayerBean, dataSource: DataSource) {
        try {
            val jukeBoxStats = JukeBoxStatisticsBean(
                player.uuid!!,
                0,
                0,
                0,
                Timestamp(System.currentTimeMillis()),
                Timestamp(System.currentTimeMillis()),
                0
            )
            connection = dataSource.connection
            val sql = "insert into jukebox_stats (uuid, mehs, woots, woots_given, creation_date, update_date, played_time) values (UNHEX(?), ?, ?, ?, now(), now(), played_time = ?)"
            statement = connection?.prepareStatement(sql)
            statement?.setString(1, Transcoder.encode(player.uuid.toString()))
            statement?.setInt(2, jukeBoxStats.mehs)
            statement?.setInt(3, jukeBoxStats.woots)
            statement?.setInt(4, jukeBoxStats.wootsGiven)
            statement?.setLong(5, jukeBoxStats.playedTime)
            statement?.executeUpdate()
        } catch (exception: Exception) {
            exception.printStackTrace()
            throw exception
        } finally {
            close()
        }
    }

    @Throws(Exception::class)
    fun updateJukeBoxStatistics(player: PlayerBean, jukeBoxStats: JukeBoxStatisticsBean, dataSource: DataSource) {
        try {
            if (getJukeBoxStatistics(player, dataSource) == null) {
                createEmptyJukeBoxStatistics(player, dataSource)
            } else {
                connection = dataSource.connection
                val sql = "update jukebox_stats set mehs = ?, woots = ?, woots_given = ?, update_date = now(), played_time = ? where uuid = UNHEX(?)"
                statement = connection?.prepareStatement(sql)
                statement?.setInt(1, jukeBoxStats.mehs)
                statement?.setInt(2, jukeBoxStats.woots)
                statement?.setInt(3, jukeBoxStats.wootsGiven)
                statement?.setLong(4, jukeBoxStats.playedTime)
                statement?.setString(5, Transcoder.encode(player.uuid.toString()))
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
            val sql = String.format(
                "select p.name as name, d.%1\$s as score from players as p, jukebox_stats as d where p.uuid = d.uuid order by d.%2\$s desc limit 3",
                category,
                category
            )
            statement = connection?.prepareStatement(sql)
            resultSet = statement?.executeQuery()
            while (resultSet?.next() == true) {
                val bean = LeaderboardBean(resultSet!!.getString("name"), resultSet!!.getInt("score"))
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
