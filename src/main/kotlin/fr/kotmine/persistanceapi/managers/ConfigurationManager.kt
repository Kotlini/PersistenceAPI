package fr.kotmine.persistanceapi.managers

import fr.kotmine.persistanceapi.models.BungeeConfigBean
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import javax.sql.DataSource

object ConfigurationManager {
    private var connection: Connection? = null
    private var statement: PreparedStatement? = null
    private var resultSet: ResultSet? = null

    @Throws(Exception::class)
    fun getConfig(dataSource: DataSource): BungeeConfigBean? {
        try {
            connection = dataSource.connection
            val sql = "select slots, motd, close_type, server_line, max_players, priority_title, welcome_message from configuration"
            statement = connection?.prepareStatement(sql)
            resultSet = statement?.executeQuery()

            if (resultSet!!.next()) {
                val slots = resultSet!!.getInt("slots")
                val motd = resultSet!!.getString("motd")
                val closeType = resultSet!!.getString("close_type")
                val serverLine = resultSet!!.getString("server_line")
                val maxPlayers = resultSet!!.getInt("max_players")
                val priorityTitle = resultSet!!.getString("priority_title")
                val welcomeMessage = resultSet!!.getString("welcome_message")

                val config = BungeeConfigBean(slots, motd, closeType, serverLine, maxPlayers, priorityTitle, welcomeMessage)
                return config
            } else {
                return null
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
            throw exception
        } finally {
            close()
        }
    }

    @Throws(Exception::class)
    fun updateConfig(config: BungeeConfigBean, dataSource: DataSource) {
        try {
            connection = dataSource.connection
            val sql = "update configuration set slots = ?, motd = ?, close_type = ?, server_line = ?, max_players = ?, priority_title = ?, welcome_message = ?"
            statement = connection?.prepareStatement(sql)
            statement?.setInt(1, config.slots)
            statement?.setString(2, config.motd)
            statement?.setString(3, config.closeType)
            statement?.setString(4, config.serverLine)
            statement?.setInt(5, config.maxPlayers)
            statement?.setString(6, config.priorityTitle)
            statement?.setString(7, config.welcomeMessage)
            statement?.executeUpdate()
        } catch (exception: Exception) {
            exception.printStackTrace()
            throw exception
        } finally {
            close()
        }
    }

    @Throws(Exception::class)
    private fun close() {
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
