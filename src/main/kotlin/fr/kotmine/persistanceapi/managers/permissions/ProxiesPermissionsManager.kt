package fr.kotmine.persistanceapi.managers.permissions

import fr.kotmine.persistanceapi.models.permissions.ProxiesPermissionsBean
import fr.kotmine.persistanceapi.models.players.PlayerBean
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import javax.sql.DataSource

object ProxiesPermissionsManager {
    // Defines
    private var connection: Connection? = null
    private var statement: PreparedStatement? = null
    private var resultSet: ResultSet? = null

    // Get the permissions for proxies
    @Throws(Exception::class)
    fun getProxiesPermissions(player: PlayerBean, dataSource: DataSource): ProxiesPermissionsBean {
        var proxiesPermissionsBean: ProxiesPermissionsBean? = null

        try {
            // Set connection
            connection = dataSource.connection

            // Query construction
            val sql = "SELECT groups_id, proxies_dispatch, proxies_global, proxies_debug, proxies_set_option, proxies_hydro" +
                    " FROM proxies_permissions WHERE groups_id = ?"

            statement = connection?.prepareStatement(sql)
            statement?.setLong(1, player.groupId!!)

            // Execute the query
            resultSet = statement?.executeQuery()

            // Manage the result in a bean
            if (resultSet?.next() == true) {
                // There's a result
                val groupId = resultSet!!.getLong("groups_id")
                val proxiesDispatch = resultSet!!.getBoolean("proxies_dispatch")
                val proxiesGlobal = resultSet!!.getBoolean("proxies_global")
                val proxiesDebug = resultSet!!.getBoolean("proxies_debug")
                val proxiesSetOption = resultSet!!.getBoolean("proxies_set_option")
                val proxiesHydro = resultSet!!.getBoolean("proxies_hydro")

                proxiesPermissionsBean = ProxiesPermissionsBean(
                    groupId, proxiesDispatch, proxiesGlobal, proxiesDebug, proxiesSetOption, proxiesHydro
                )
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
            throw exception
        } finally {
            // Close the query environment to prevent leaks
            close()
        }

        return proxiesPermissionsBean!!
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
