package fr.kotmine.persistanceapi.managers.players

import fr.kotmine.persistanceapi.models.players.PlayerBean
import fr.kotmine.persistanceapi.models.players.PlayerSettingsBean
import fr.kotmine.persistanceapi.utils.Transcoder
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.util.UUID
import javax.sql.DataSource

object PlayerSettingsManager {
    private var connection: Connection? = null
    private var statement: PreparedStatement? = null
    private var resultSet: ResultSet? = null

    @Throws(Exception::class)
    fun getPlayerSettings(player: PlayerBean, dataSource: DataSource): PlayerSettingsBean {
        val playerSettingsBean: PlayerSettingsBean?

        try {
            connection = dataSource.connection

            var sql = ""
            sql += "select HEX(uuid) as uuid, jukebox_listen, group_demand_receive, friendship_demand_receive, notification_receive, private_message_receive, chat_visible, player_visible"
            sql += ", waiting_line_notification, other_player_interaction, click_on_me_activation, allow_statistic_onclick, allow_coins_onclick, allow_powders_onclick, allow_click_on_other, elytra_activated"
            sql += " from player_settings where uuid = UNHEX(?)"

            statement = connection!!.prepareStatement(sql)
            statement!!.setString(1, Transcoder.encode(player.uuid.toString()))

            resultSet = statement!!.executeQuery()

            if (resultSet!!.next()) {
                val playerUuid = Transcoder.decode(resultSet!!.getString("uuid"))
                val uuid = UUID.fromString(playerUuid)
                val jukeboxListen = resultSet!!.getBoolean("jukebox_listen")
                val groupDemandReceive = resultSet!!.getBoolean("group_demand_receive")
                val friendshipDemandReceive = resultSet!!.getBoolean("friendship_demand_receive")
                val notificationReceive = resultSet!!.getBoolean("notification_receive")
                val privateMessageReceive = resultSet!!.getBoolean("private_message_receive")
                val chatVisible = resultSet!!.getBoolean("chat_visible")
                val playerVisible = resultSet!!.getBoolean("player_visible")
                val waitingLineNotification = resultSet!!.getBoolean("waiting_line_notification")
                val otherPlayerInteraction = resultSet!!.getBoolean("other_player_interaction")
                val clickOnMeActivation = resultSet!!.getBoolean("click_on_me_activation")
                val allowStatisticOnClick = resultSet!!.getBoolean("allow_statistic_onclick")
                val allowCoinsOnClick = resultSet!!.getBoolean("allow_coins_onclick")
                val allowPowdersOnClick = resultSet!!.getBoolean("allow_powders_onclick")
                val allowClickOnOther = resultSet!!.getBoolean("allow_click_on_other")
                playerSettingsBean = PlayerSettingsBean(
                    uuid,
                    jukeboxListen,
                    groupDemandReceive,
                    friendshipDemandReceive,
                    notificationReceive,
                    privateMessageReceive,
                    chatVisible,
                    playerVisible,
                    waitingLineNotification,
                    otherPlayerInteraction,
                    clickOnMeActivation,
                    allowStatisticOnClick,
                    allowCoinsOnClick,
                    allowPowdersOnClick,
                    allowClickOnOther
                )
                return playerSettingsBean
            } else {
                close()
                createDefaultPlayerSettings(player, dataSource)
                val settings = getPlayerSettings(player, dataSource)
                close()
                return settings
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
            throw exception
        } finally {
            close()
        }
    }

    @Throws(Exception::class)
    fun setPlayerSettings(player: PlayerBean, settingsBeans: PlayerSettingsBean, dataSource: DataSource) {
        try {
            connection = dataSource.connection

            var sql = ""
            sql += "update player_settings set jukebox_listen = ?, group_demand_receive = ?, friendship_demand_receive = ?, notification_receive = ?"
            sql += ", private_message_receive = ?, chat_visible = ?, player_visible = ?, waiting_line_notification = ?, other_player_interaction = ?"
            sql += ", click_on_me_activation = ?, allow_statistic_onclick = ?, allow_coins_onclick = ?, allow_powders_onclick = ?"
            sql += ", allow_click_on_other = ?, elytra_activated = ? where uuid = UNHEX(?)"

            statement = connection!!.prepareStatement(sql)
            statement!!.setBoolean(1, settingsBeans.jukeboxListen)
            statement!!.setBoolean(2, settingsBeans.groupDemandReceive)
            statement!!.setBoolean(3, settingsBeans.friendshipDemandReceive)
            statement!!.setBoolean(4, settingsBeans.notificationReceive)
            statement!!.setBoolean(5, settingsBeans.privateMessageReceive)
            statement!!.setBoolean(6, settingsBeans.chatVisible)
            statement!!.setBoolean(7, settingsBeans.playerVisible)
            statement!!.setBoolean(8, settingsBeans.waitingLineNotification)
            statement!!.setBoolean(9, settingsBeans.otherPlayerInteraction)
            statement!!.setBoolean(10, settingsBeans.clickOnMeActivation)
            statement!!.setBoolean(11, settingsBeans.allowStatisticOnClick)
            statement!!.setBoolean(12, settingsBeans.allowCoinsOnClick)
            statement!!.setBoolean(13, settingsBeans.allowPowdersOnClick)
            statement!!.setBoolean(14, settingsBeans.allowClickOnOther)
            statement!!.setString(16, Transcoder.encode(player.uuid.toString()))

            statement!!.executeUpdate()
        } catch (exception: Exception) {
            exception.printStackTrace()
            throw exception
        } finally {
            close()
        }
    }

    @Throws(Exception::class)
    fun createDefaultPlayerSettings(player: PlayerBean, dataSource: DataSource) {
        try {
            connection = dataSource.connection

            var sql = "insert into player_settings (uuid, jukebox_listen, group_demand_receive, friendship_demand_receive, notification_receive, private_message_receive"
            sql += ", chat_visible, player_visible, waiting_line_notification, other_player_interaction, click_on_me_activation, allow_statistic_onclick, allow_coins_onclick"
            sql += ", allow_powders_onclick, allow_click_on_other, elytra_activated)"
            sql += " values (UNHEX(?), true, true, true, true, true, true, true, true, true, true, true, true, true, true, true)"

            statement = connection!!.prepareStatement(sql)
            statement!!.setString(1, Transcoder.encode(player.uuid.toString()))

            statement!!.executeUpdate()
        } catch (exception: Exception) {
            exception.printStackTrace()
            throw exception
        } finally {
            close()
        }
    }

    @Throws(Exception::class)
    fun close() {
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
