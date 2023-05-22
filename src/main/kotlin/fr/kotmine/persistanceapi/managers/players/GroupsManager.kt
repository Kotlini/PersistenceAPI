package fr.kotmine.persistanceapi.managers.players

import fr.kotmine.persistanceapi.models.players.GroupsBean
import fr.kotmine.persistanceapi.models.players.PlayerBean
import javax.sql.DataSource
import java.sql.*

object GroupsManager {
    // Defines
    private var connection: Connection? = null
    private var statement: PreparedStatement? = null
    private var resultSet: ResultSet? = null
    private var groupsBean: GroupsBean? = null

    // Get the permission group for a player
    @Throws(Exception::class)
    fun getPlayerGroup(player: PlayerBean, dataSource: DataSource): GroupsBean {
        try {
            // Set connection
            connection = dataSource.connection

            // Query construction
            val sql = "select group_id, group_name, rank, tag, prefix, suffix, multiplier from groups where group_id = ?"

            statement = connection?.prepareStatement(sql)
            statement?.setLong(1, player.groupId!!)

            // Execute the query
            resultSet = statement?.executeQuery()

            // Manage the result in a bean
            if (resultSet?.next() == true) {
                // There's a result
                val groupId = resultSet?.getLong("group_id") ?: 0
                val groupName = resultSet?.getString("group_name")
                val rank = resultSet?.getInt("rank") ?: 0
                val tag = resultSet?.getString("tag")
                val prefix = resultSet?.getString("prefix")
                val suffix = resultSet?.getString("suffix")
                val multiplier = resultSet?.getInt("multiplier") ?: 0
                groupsBean = GroupsBean(groupId, groupName!!, rank, tag!!, prefix!!, suffix!!, multiplier)
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
            throw exception
        } finally {
            // Close the query environment in order to prevent leaks
            close()
        }
        return groupsBean!!
    }

    // Close all connection
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
