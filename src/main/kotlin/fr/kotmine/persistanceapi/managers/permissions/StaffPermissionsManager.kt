package fr.kotmine.persistanceapi.managers.permissions

import fr.kotmine.persistanceapi.models.permissions.StaffPermissionsBean
import fr.kotmine.persistanceapi.models.players.PlayerBean
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import javax.sql.DataSource

object StaffPermissionsManager {
    // Defines
    private var connection: Connection? = null
    private var statement: PreparedStatement? = null
    private var resultSet: ResultSet? = null

    // Get the permissions for staff
    @Throws(Exception::class)
    fun getStaffPermissions(player: PlayerBean, dataSource: DataSource): StaffPermissionsBean {
        var staffPermissionsBean: StaffPermissionsBean? = null

        try {
            // Set connection
            connection = dataSource.connection

            // Query construction
            val sql = "SELECT groups_id, netjoin_closed, netjoin_vip, netjoin_full, tracker_famous, network_vip, network_vip_plus, network_staff, network_admin" +
                    " FROM staff_permissions WHERE groups_id = ?"

            statement = connection?.prepareStatement(sql)
            statement?.setLong(1, player.groupId!!)

            // Execute the query
            resultSet = statement?.executeQuery()

            // Manage the result in a bean
            if (resultSet?.next() == true) {
                // There's a result
                val groupsId = resultSet!!.getLong("groups_id")
                val netjoinClosed = resultSet!!.getBoolean("netjoin_closed")
                val netjoinVip = resultSet!!.getBoolean("netjoin_vip")
                val netjoinFull = resultSet!!.getBoolean("netjoin_full")
                val trackerFamous = resultSet!!.getBoolean("tracker_famous")
                val networkVip = resultSet!!.getBoolean("network_vip")
                val networkVipplus = resultSet!!.getBoolean("network_vip_plus")
                val networkStaff = resultSet!!.getBoolean("network_staff")
                val networkAdmin = resultSet!!.getBoolean("network_admin")

                staffPermissionsBean = StaffPermissionsBean(
                    groupsId, netjoinClosed, netjoinVip, netjoinFull, trackerFamous, networkVip, networkVipplus, networkStaff, networkAdmin
                )
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
            throw exception
        } finally {
            // Close the query environment to prevent leaks
            close()
        }

        return staffPermissionsBean!!
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
