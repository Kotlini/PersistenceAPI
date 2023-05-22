package fr.kotmine.persistanceapi.managers.permissions

import fr.kotmine.persistanceapi.models.permissions.BungeeRedisPermissionsBean
import fr.kotmine.persistanceapi.models.players.PlayerBean
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import javax.sql.DataSource

object BungeeRedisPermissionsManager {
    // Defines
    private var connection: Connection? = null
    private var statement: PreparedStatement? = null
    private var resultset: ResultSet? = null

    // Get the permissions for Bungee & Redis
    @Throws(Exception::class)
    fun getBungeeRedisPermissions(player: PlayerBean, dataSource: DataSource): BungeeRedisPermissionsBean {
        var bungeeRedisPermissionsBean: BungeeRedisPermissionsBean? = null

        try {
            // Set connection
            connection = dataSource.connection

            // Query construction
            val sql = "SELECT groups_id, bungeecord_command_list, bungeecord_command_find, redisbungee_command_lastseen, bungeecord_command_ip, redisbungee_command_sendtoall" +
                    ", redisbungee_command_serverid, redisbunge_command_serverids, redisbungee_command_pproxy, redisbungee_command_plist, bungeecord_command_server, bungeecord_command_send" +
                    ", bungeecord_command_end, bungeecord_command_alert" +
                    " FROM bungee_redis_permissions WHERE groups_id = ?"

            statement = connection!!.prepareStatement(sql)
            statement!!.setLong(1, player.groupId!!)

            // Execute the query
            resultset = statement!!.executeQuery()

            // Manage the result in a bean
            if (resultset!!.next()) {
                // There's a result
                val groupId = resultset!!.getLong("groups_id")
                val bungeecordCommandList = resultset!!.getBoolean("bungeecord_command_list")
                val bungeecordCommandFind = resultset!!.getBoolean("bungeecord_command_find")
                val redisbungeeCommandLastSeen = resultset!!.getBoolean("redisbungee_command_lastseen")
                val redisbungeeCommandSendtoAll = resultset!!.getBoolean("bungeecord_command_ip")
                val bungeecordCommandIp = resultset!!.getBoolean("redisbungee_command_sendtoall")
                val redisbungeeCommandServerId = resultset!!.getBoolean("redisbungee_command_serverid")
                val redisbungeCommandServerIds = resultset!!.getBoolean("redisbunge_command_serverids")
                val redisbungeeCommandPproxy = resultset!!.getBoolean("redisbungee_command_pproxy")
                val redisbungeeCommandPlist = resultset!!.getBoolean("redisbungee_command_plist")
                val bungeecordCommandServer = resultset!!.getBoolean("bungeecord_command_server")
                val bungeecordCommandSend = resultset!!.getBoolean("bungeecord_command_send")
                val bungeecordCommandEnd = resultset!!.getBoolean("bungeecord_command_end")
                val bungeecordCommandAlert = resultset!!.getBoolean("bungeecord_command_alert")

                bungeeRedisPermissionsBean = BungeeRedisPermissionsBean(
                    groupId,
                    bungeecordCommandList,
                    bungeecordCommandFind,
                    redisbungeeCommandLastSeen,
                    redisbungeeCommandSendtoAll,
                    bungeecordCommandIp,
                    redisbungeeCommandServerId,
                    redisbungeCommandServerIds,
                    redisbungeeCommandPproxy,
                    redisbungeeCommandPlist,
                    bungeecordCommandServer,
                    bungeecordCommandSend,
                    bungeecordCommandEnd,
                    bungeecordCommandAlert
                )
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
            throw exception
        } finally {
            // Close the query environment in order to prevent leaks
            close()
        }

        return bungeeRedisPermissionsBean!!
    }

    // Close all connections
    @Throws(Exception::class)
    fun close() {
        // Close the query environment in order to prevent leaks
        try {
            resultset?.close()
            statement?.close()
            connection?.close()
        } catch (exception: Exception) {
            exception.printStackTrace()
            throw exception
        }
    }
}
