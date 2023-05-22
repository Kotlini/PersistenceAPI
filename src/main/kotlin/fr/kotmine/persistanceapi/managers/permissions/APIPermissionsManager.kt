package fr.kotmine.persistanceapi.managers.permissions

import fr.kotmine.persistanceapi.models.permissions.APIPermissionsBean
import fr.kotmine.persistanceapi.models.players.PlayerBean
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import javax.sql.DataSource

object APIPermissionsManager {
    // Defines
    private var connection: Connection? = null
    private var statement: PreparedStatement? = null
    private var resultset: ResultSet? = null

    // Get the permissions for the API
    @Throws(Exception::class)
    fun getAPIPermissions(player: PlayerBean, dataSource: DataSource): APIPermissionsBean {
        var apiPermissionsBean: APIPermissionsBean? = null

        try {
            // Set connection
            connection = dataSource.connection

            // Query construction
            val sql = "SELECT groups_id, api_servers_debug, api_permissions_refresh, api_coins_getother, api_coins_credit, api_coins_withdraw, api_inventory_show" +
                    ", api_playerdata_show, api_playerdata_set, api_playerdata_del, api_modo_speakup, api_stars_getother, api_stars_credit, api_stars_withdraw, api_game_start, api_chat_bypass" +
                    " FROM api_permissions WHERE groups_id = ?"

            statement = connection!!.prepareStatement(sql)
            statement!!.setLong(1, player.groupId!!)

            // Execute the query
            resultset = statement!!.executeQuery()

            // Manage the result in a bean
            if (resultset!!.next()) {
                // There's a result
                val groupId = resultset!!.getLong("groups_id")
                val apiServersDebug = resultset!!.getBoolean("api_servers_debug")
                val apiPermissionsRefresh = resultset!!.getBoolean("api_permissions_refresh")
                val apiCoinsGetOther = resultset!!.getBoolean("api_coins_getother")
                val apiCoinsCredit = resultset!!.getBoolean("api_coins_credit")
                val apiCoinsWithdraw = resultset!!.getBoolean("api_coins_withdraw")
                val apiInventoryShow = resultset!!.getBoolean("api_inventory_show")
                val apiPlayerDataShow = resultset!!.getBoolean("api_playerdata_show")
                val apiPlayerdataSet = resultset!!.getBoolean("api_playerdata_set")
                val apiPlayerdataDel = resultset!!.getBoolean("api_playerdata_del")
                val apiModoSpeakup = resultset!!.getBoolean("api_modo_speakup")
                val apiStarsGetother = resultset!!.getBoolean("api_stars_getother")
                val apiStarsCredit = resultset!!.getBoolean("api_stars_credit")
                val apiStarsWithdraw = resultset!!.getBoolean("api_stars_withdraw")
                val apiGameStart = resultset!!.getBoolean("api_game_start")
                val apiChatBypass = resultset!!.getBoolean("api_chat_bypass")

                apiPermissionsBean = APIPermissionsBean(
                    groupId, apiServersDebug, apiPermissionsRefresh, apiCoinsGetOther, apiCoinsCredit, apiCoinsWithdraw, apiInventoryShow,
                    apiPlayerDataShow, apiPlayerdataSet, apiPlayerdataDel, apiModoSpeakup, apiStarsGetother, apiStarsCredit, apiStarsWithdraw, apiGameStart, apiChatBypass
                )
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
            throw exception
        } finally {
            // Close the query environment in order to prevent leaks
            close()
        }

        return apiPermissionsBean!!
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
