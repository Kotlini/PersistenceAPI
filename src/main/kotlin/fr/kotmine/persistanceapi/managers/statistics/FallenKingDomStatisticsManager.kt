package fr.kotmine.persistanceapi.managers.statistics

import fr.kotmine.persistanceapi.models.players.PlayerBean
import fr.kotmine.persistanceapi.models.statistics.FallenKingdomStatisticsBean
import fr.kotmine.persistanceapi.models.statistics.LeaderboardBean
import fr.kotmine.persistanceapi.utils.Transcoder
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Timestamp
import java.util.*
import javax.sql.DataSource
import kotlin.collections.ArrayList

object FallenKingDomStatisticsManager {

    private var connection: Connection? = null
    private var statement: PreparedStatement? = null
    private var resultSet: ResultSet? = null

    @Throws(Exception::class)
    fun getFallenKingDomStatistics(player: PlayerBean, dataSource: DataSource): FallenKingdomStatisticsBean {
        val fallenStats: FallenKingdomStatisticsBean?

        try {
            // Set connection
            connection = dataSource.connection

            // Query construction
            val sql = "select HEX(uuid) as uuid,  deaths, kills, heart_kills, played_games, wins, creation_date, update_date, played_time from fallen_stats where uuid = UNHEX(?)"

            statement = connection?.prepareStatement(sql)
            statement?.setString(1, Transcoder.encode(player.uuid.toString()))

            // Execute the query
            resultSet = statement?.executeQuery()

            // Manage the result in a bean
            if (resultSet!!.next()) {
                // There's a result
                val playerUuid = Transcoder.decode(resultSet!!.getString("uuid"))
                val uuid = UUID.fromString(playerUuid)
                val deaths = resultSet!!.getInt("deaths")
                val kills = resultSet!!.getInt("kills")
                val heartKills = resultSet!!.getInt("heart_kills")
                val playedGames = resultSet!!.getInt("played_games")
                val wins = resultSet!!.getInt("wins")
                val creationDate = resultSet!!.getTimestamp("creation_date")
                val updateDate = resultSet!!.getTimestamp("update_date")
                val playedTime = resultSet!!.getLong("played_time")

                fallenStats = FallenKingdomStatisticsBean(
                    uuid,
                    deaths,
                    kills,
                    heartKills,
                    playedGames,
                    wins,
                    creationDate,
                    updateDate,
                    playedTime
                )
            } else {
                // If there's no uhc stats in the database, create an empty one
                close()
                createEmptyFallenKingDomStatistics(player, dataSource)
                close()

                val newUHCStats = getFallenKingDomStatistics(player, dataSource)
                close()

                return newUHCStats
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
            throw exception
        } finally {
            // Close the query environment to prevent leaks
            close()
        }

        return fallenStats!!
    }

    @Throws(Exception::class)
    private fun createEmptyFallenKingDomStatistics(player: PlayerBean, dataSource: DataSource) {
        try {
            // Create an empty bean
            val fallenStats = FallenKingdomStatisticsBean(
                player.uuid!!,
                0,
                0,
                0,
                0,
                0,
                Timestamp(System.currentTimeMillis()),
                Timestamp(System.currentTimeMillis()),
                0L
            )

            // Set connection
            connection = dataSource.connection

            // Query construction for create
            val sql = "insert into fallen_stats (uuid, deaths, kills, heart_kills, played_games, wins, creation_date, update_date, played_time) values (UNHEX(?), ?, ?, ?, ?, ?, now(), now(), ?)"

            statement = connection?.prepareStatement(sql)
            statement?.setString(1, Transcoder.encode(player.uuid.toString()))
            statement?.setInt(2, fallenStats.deaths)
            statement?.setInt(3, fallenStats.kills)
            statement?.setInt(4, fallenStats.heartKills)
            statement?.setInt(5, fallenStats.playedGames)
            statement?.setInt(6, fallenStats.wins)
            statement?.setLong(7, fallenStats.playedTime)

            // Execute the query
            statement?.executeUpdate()
        } catch (exception: Exception) {
            exception.printStackTrace()
            throw exception
        } finally {
            // Close the query environment to prevent leaks
            close()
        }
    }

    @Throws(Exception::class)
    fun updateFallenKingDomStatistics(player: PlayerBean, fallenStats: FallenKingdomStatisticsBean, dataSource: DataSource) {
        try {
            // Check if a record exists
            if (getFallenKingDomStatistics(player, dataSource) == null) {
                // Create an empty uhc statistics
                createEmptyFallenKingDomStatistics(player, dataSource)
            } else {
                // Set connection
                connection = dataSource.connection

                // Query construction for update
                val sql = "update fallen_stats set deaths = ?, kills = ?, heart_kills = ?, played_games = ?, wins = ?, update_date = now(), played_time = ? where uuid = UNHEX(?)"

                statement = connection!!.prepareStatement(sql)
                statement?.setInt(1, fallenStats.deaths)
                statement?.setInt(2, fallenStats.kills)
                statement?.setInt(3, fallenStats.heartKills)
                statement?.setInt(4, fallenStats.playedGames)
                statement?.setInt(5, fallenStats.wins)
                statement?.setLong(6, fallenStats.playedTime)
                statement?.setString(6, Transcoder.encode(player.uuid.toString()))

                // Execute the query
                statement?.executeUpdate()
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
            throw exception
        } finally {
            // Close the query environment to prevent leaks
            close()
        }
    }

    @Throws(Exception::class)
    fun getLeaderBoard(category: String, dataSource: DataSource): List<LeaderboardBean> {
        val leaderBoard: MutableList<LeaderboardBean> = ArrayList()
        try {
            // Set connection
            connection = dataSource.connection

            // Query construction
            val sql =
                String.format("select p.name as name, d.%1\$s as score from players as p, fallen_stats as d where p.uuid = d.uuid order by d.%2\$s desc limit 3",
                    category,
                    category
                )

            statement = connection!!.prepareStatement(sql)

            // Execute the query
            resultSet = statement!!.executeQuery()

            // Manage the result in a bean
            while (resultSet!!.next()) {
                val bean = LeaderboardBean(resultSet!!.getString("name"), resultSet!!.getInt("score"))
                leaderBoard.add(bean)
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
            throw exception
        } finally {
            // Close the query environment to prevent leaks
            close()
        }
        return leaderBoard
    }

    @Throws(Exception::class)
    fun close() {
        // Close the query environment to prevent leaks
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