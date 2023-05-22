package fr.kotmine.persistanceapi.managers.permissions

import fr.kotmine.persistanceapi.models.permissions.ModerationPermissionsBean
import fr.kotmine.persistanceapi.models.players.PlayerBean
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import javax.sql.DataSource

object ModerationPermissionsManager {
    // Defines
    private var connection: Connection? = null
    private var statement: PreparedStatement? = null
    private var resultSet: ResultSet? = null

    // Get the permissions for moderation
    @Throws(Exception::class)
    fun getModerationPermissions(player: PlayerBean, dataSource: DataSource): ModerationPermissionsBean {
        var moderationPermissionsBean: ModerationPermissionsBean? = null

        try {
            // Set connection
            connection = dataSource.connection

            // Query construction
            val sql = "SELECT groups_id, mod_ban, mod_tp, mod_kick, mod_pardon, mod_mute_longtime, mod_mute, mod_channel, mod_channel_report, mod_quiet" +
                    " FROM moderation_permissions WHERE groups_id = ?"

            statement = connection?.prepareStatement(sql)
            statement?.setLong(1, player.groupId!!)

            // Execute the query
            resultSet = statement?.executeQuery()

            // Manage the result in a bean
            if (resultSet?.next() == true) {
                // There's a result
                val groupId = resultSet!!.getLong("groups_id")
                val modBan = resultSet!!.getBoolean("mod_ban")
                val modTp = resultSet!!.getBoolean("mod_tp")
                val modKick = resultSet!!.getBoolean("mod_kick")
                val modPardon = resultSet!!.getBoolean("mod_pardon")
                val modMuteLongtime = resultSet!!.getBoolean("mod_mute_longtime")
                val modMute = resultSet!!.getBoolean("mod_mute")
                val modChannel = resultSet!!.getBoolean("mod_channel")
                val modChannelReport = resultSet!!.getBoolean("mod_channel_report")
                val modQuiet = resultSet!!.getBoolean("mod_quiet")

                moderationPermissionsBean = ModerationPermissionsBean(
                    groupId, modBan, modTp, modKick, modPardon, modMuteLongtime, modMute, modChannel, modChannelReport, modQuiet
                )
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
            throw exception
        } finally {
            // Close the query environment to prevent leaks
            close()
        }

        return moderationPermissionsBean!!
    }

    // Close all connections
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
