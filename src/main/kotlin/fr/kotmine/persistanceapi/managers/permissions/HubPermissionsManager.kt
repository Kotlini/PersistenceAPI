package fr.kotmine.persistanceapi.managers.permissions

import fr.kotmine.persistanceapi.models.permissions.HubPermissionsBean
import fr.kotmine.persistanceapi.models.players.PlayerBean
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import javax.sql.DataSource

object HubPermissionsManager {
    // Defines
    private var connection: Connection? = null
    private var statement: PreparedStatement? = null
    private var resultSet: ResultSet? = null

    // Get the permissions for the hub
    @Throws(Exception::class)
    fun getHubPermissions(player: PlayerBean, dataSource: DataSource): HubPermissionsBean {
        var hubPermissionsBean: HubPermissionsBean? = null

        try {
            // Set connection
            connection = dataSource.connection

            // Query construction
            val sql = "SELECT groups_id, hub_jukebox_lock, hub_jukebox_next, hub_jukebox_clear, hub_mod_slow, hub_mod_shutup, hub_admin_npc, hub_admin_sign, hub_anguille" +
                    ", hub_jukebox_nbs, hub_admin_evacuate, hub_announce, hub_gadgets_cooldownbypass, hub_gadgets_nuke, hub_jukebox_limitbypass, hub_jukebox_limitstaff" +
                    ", hub_bypassmute, hub_fly, hub_debug_sign, hub_sign_selection, hub_beta_vip, hub_admin_pearl, hub_animating_event" +
                    " FROM hub_permissions WHERE groups_id = ?"

            statement = connection?.prepareStatement(sql)
            statement?.setLong(1, player.groupId!!)

            // Execute the query
            resultSet = statement?.executeQuery()

            // Manage the result in a bean
            if (resultSet?.next() == true) {
                // There's a result
                val groupId = resultSet!!.getLong("groups_id")
                val hubJukeboxLock = resultSet!!.getBoolean("hub_jukebox_lock")
                val hubJukeboxNext = resultSet!!.getBoolean("hub_jukebox_next")
                val hubJukeBoxClear = resultSet!!.getBoolean("hub_jukebox_clear")
                val hubModSlow = resultSet!!.getBoolean("hub_mod_slow")
                val hubModShutup = resultSet!!.getBoolean("hub_mod_shutup")
                val hubAdminNpc = resultSet!!.getBoolean("hub_admin_npc")
                val hubAdminSign = resultSet!!.getBoolean("hub_admin_sign")
                val hubAnguille = resultSet!!.getBoolean("hub_anguille")
                val hubJukeboxNbs = resultSet!!.getBoolean("hub_jukebox_nbs")
                val hubAdminEvacuate = resultSet!!.getBoolean("hub_admin_evacuate")
                val hubAnnounce = resultSet!!.getBoolean("hub_announce")
                val hubGadgetsCooldownbypass = resultSet!!.getBoolean("hub_gadgets_cooldownbypass")
                val hubGadgetsNuke = resultSet!!.getBoolean("hub_gadgets_nuke")
                val hubJukeboxLimitbypass = resultSet!!.getBoolean("hub_jukebox_limitbypass")
                val hubJukeboxLimitstaff = resultSet!!.getBoolean("hub_jukebox_limitstaff")
                val hubBypassmute = resultSet!!.getBoolean("hub_bypassmute")
                val hubFly = resultSet!!.getBoolean("hub_fly")
                val hubDebugSign = resultSet!!.getBoolean("hub_debug_sign")
                val hubSignSelection = resultSet!!.getBoolean("hub_sign_selection")
                val hubBetaVIP = resultSet!!.getBoolean("hub_beta_vip")
                val hubAdminPearl = resultSet!!.getBoolean("hub_admin_pearl")
                val hubAnimatingEvent = resultSet!!.getBoolean("hub_animating_event")

                hubPermissionsBean = HubPermissionsBean(
                    groupId, hubJukeboxLock, hubJukeboxNext, hubJukeBoxClear, hubModSlow, hubModShutup, hubAdminNpc, hubAdminSign, hubAnguille,
                    hubJukeboxNbs, hubAdminEvacuate, hubAnnounce, hubGadgetsCooldownbypass, hubGadgetsNuke, hubJukeboxLimitbypass, hubJukeboxLimitstaff, hubBypassmute, hubFly,
                    hubDebugSign, hubSignSelection, hubBetaVIP, hubAdminPearl, hubAnimatingEvent
                )
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
            throw exception
        } finally {
            // Close the query environment to prevent leaks
            close()
        }

        return hubPermissionsBean!!
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
