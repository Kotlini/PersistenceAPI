package fr.kotmine.persistanceapi.managers.permissions

import fr.kotmine.persistanceapi.models.permissions.BukkitPermissionsBean
import fr.kotmine.persistanceapi.models.players.PlayerBean
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import javax.sql.DataSource

object BukkitPermissionsManager {
    // Defines
    private var connection: Connection? = null
    private var statement: PreparedStatement? = null
    private var resultset: ResultSet? = null

    // Get the permissions for Bukkit
    @Throws(Exception::class)
    fun getBukkitPermissions(player: PlayerBean, dataSource: DataSource): BukkitPermissionsBean {
        var bukkitPermissionsBean: BukkitPermissionsBean? = null

        try {
            // Set connection
            connection = dataSource.connection

            // Query construction
            val sql = "SELECT groups_id, minecraft_command_op, bukkit_command_op_give, bukkit_command_effect, bukkit_command_gamemode, bukkit_command_teleport" +
                    " FROM bukkit_permissions WHERE groups_id = ?"

            statement = connection!!.prepareStatement(sql)
            statement!!.setLong(1, player.groupId!!)

            // Execute the query
            resultset = statement!!.executeQuery()

            // Manage the result in a bean
            if (resultset!!.next()) {
                // There's a result
                val groupId = resultset!!.getLong("groups_id")
                val minecraftCommandOp = resultset!!.getBoolean("minecraft_command_op")
                val bukkitCommandOpGive = resultset!!.getBoolean("bukkit_command_op_give")
                val bukkitCommandEffect = resultset!!.getBoolean("bukkit_command_effect")
                val bukkitCommandGamemode = resultset!!.getBoolean("bukkit_command_gamemode")
                val bukkitCommandTeleport = resultset!!.getBoolean("bukkit_command_teleport")

                bukkitPermissionsBean = BukkitPermissionsBean(
                    groupId,
                    minecraftCommandOp,
                    bukkitCommandOpGive,
                    bukkitCommandEffect,
                    bukkitCommandGamemode,
                    bukkitCommandTeleport
                )
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
            throw exception
        } finally {
            // Close the query environment in order to prevent leaks
            close()
        }

        return bukkitPermissionsBean!!
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
