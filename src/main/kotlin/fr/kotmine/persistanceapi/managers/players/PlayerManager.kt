package fr.kotmine.persistanceapi.managers.players

import fr.kotmine.persistanceapi.models.players.PlayerBean
import fr.kotmine.persistanceapi.utils.Transcoder
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Timestamp
import java.util.UUID
import javax.sql.DataSource

object PlayerManager {
    // Defines
    private var connection: Connection? = null
    private var statement: PreparedStatement? = null
    private var resultSet: ResultSet? = null

    // Get player by UUID, create if unknown
    @Throws(Exception::class)
    fun getPlayer(uuid: UUID, player: PlayerBean, dataSource: DataSource): PlayerBean {
        // Make the research of player by UUID
        try {
            // Set connection
            connection = dataSource.connection

            // Query construction
            val sql = "SELECT HEX(uuid) AS uuid, name, nickname, coins, stars, powders, last_login, first_login, last_ip, toptp_key, group_id FROM players WHERE uuid = UNHEX(?)"

            statement = connection?.prepareStatement(sql)
            statement?.setString(1, Transcoder.encode(uuid.toString()))

            // Execute the query
            resultSet = statement?.executeQuery()

            // Manage the result in a bean
            if (resultSet?.next() == true) {
                // There's a result
                val playerUuid = Transcoder.decode(resultSet!!.getString("uuid"))
                val name = resultSet?.getString("name") ?: "null"
                val nickName = resultSet?.getString("nickname") ?: "null"
                val coins = resultSet?.getInt("coins") ?: 0
                val stars = resultSet?.getInt("stars") ?: 0
                val powders = resultSet?.getInt("powders") ?: 0
                val lastLogin = resultSet?.getTimestamp("last_login") ?: Timestamp(0)
                val firsLogin = resultSet?.getTimestamp("first_login") ?: Timestamp(0)
                val lastIP = resultSet?.getString("last_ip") ?: "null"
                val toptpKey = resultSet?.getString("toptp_key") ?: "null"
                val groupId = resultSet?.getLong("group_id") ?: 0L
                return PlayerBean(
                    UUID.fromString(playerUuid),
                    name,
                    nickName,
                    coins,
                    stars,
                    powders,
                    lastLogin,
                    firsLogin,
                    lastIP,
                    toptpKey,
                    groupId
                )
            } else {
                // If there no player for the uuid in database create a new player
                close()
                createPlayer(player, dataSource)
                val newPlayer = getPlayer(uuid, player, dataSource)
                close()
                return newPlayer
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
            throw exception
        } finally {
            // Close the query environment in order to prevent leaks
            close()
        }
    }

    // Try to recover a suspect UUID by name
    @Throws(Exception::class)
    fun recoverSuspect(suspectName: String, dataSource: DataSource): UUID? {
        // Defines
        var player: PlayerBean? = null
        var suspectUUID: UUID? = null

        // Try to find the player
        try {
            // Set connection
            connection = dataSource.connection

            // Query construction
            val sql = "SELECT HEX(uuid) AS uuid FROM players WHERE name = ?"

            statement = connection?.prepareStatement(sql)
            statement?.setString(1, suspectName)

            // Execute the query
            resultSet = statement?.executeQuery()

            // Manage the result in a bean
            return if (resultSet?.next() == true) {
                // There's a result
                val playerUuid = Transcoder.decode(resultSet!!.getString("uuid"))
                suspectUUID = UUID.fromString(playerUuid)
                suspectUUID
            } else null
        } catch (exception: Exception) {
            exception.printStackTrace()
            throw exception
        } finally {
            // Close the query environment in order to prevent leaks
            close()
        }
    }

    // Update the player data
    @Throws(Exception::class)
    fun updatePlayer(player: PlayerBean, dataSource: DataSource) {
        // Update the players data
        try {
            // Set connection
            connection = dataSource.connection

            // Query construction
            val sql =
                "UPDATE players SET coins = ?, name = ?, stars = ?, powders = ?, last_login = ?, last_ip = ?, toptp_key = ?, group_id = ?, nickname = ? WHERE uuid = UNHEX(?)"

            statement = connection?.prepareStatement(sql)
            statement?.setInt(1, player.coins!!)
            statement?.setString(2, player.name)
            statement?.setInt(3, player.stars!!)
            statement?.setInt(4, player.powders!!)
            statement?.setString(5, player.lastLogin.toString())
            statement?.setString(6, player.lastIP)
            statement?.setString(7, player.topTpKey)
            statement?.setLong(8, player.groupId!!)
            statement?.setString(9, player.nickName)
            statement?.setString(10, Transcoder.encode(player.uuid.toString()))

            // Execute the query
            statement?.executeUpdate()
        } catch (exception: Exception) {
            exception.printStackTrace()
            throw exception
        } finally {
            // Close the query environment in order to prevent leaks
            close()
        }
    }

    // Create the player
    @Throws(Exception::class)
    fun createPlayer(player: PlayerBean, dataSource: DataSource) {
        // Create the player
        try {
            // Set connection
            connection = dataSource.connection

            // Query construction
            val sql =
                "INSERT INTO players (uuid, name, nickname, coins, stars, powders, last_login, first_login, last_ip, toptp_key, group_id) VALUES (UNHEX(?), ?, ?, ?, ?, ?, now(), now(), ?, ?, ?)"

            statement = connection?.prepareStatement(sql)
            statement?.setString(1, Transcoder.encode(player.uuid.toString()))
            statement?.setString(2, player.name)
            statement?.setString(3, player.nickName)
            statement?.setInt(4, player.coins!!)
            statement?.setInt(5, player.stars!!)
            statement?.setInt(6, player.powders!!)
            statement?.setString(7, player.lastIP)
            statement?.setString(8, player.topTpKey)
            statement?.setLong(9, player.groupId!!)

            // Execute the query
            statement?.executeUpdate()
        } catch (exception: Exception) {
            exception.printStackTrace()
            throw exception
        } finally {
            // Close the query environment in order to prevent leaks
            close()
        }
    }

    // Close the connection
    @Throws(Exception::class)
    fun close() {
        // Close the query environment in order to prevent leaks
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
