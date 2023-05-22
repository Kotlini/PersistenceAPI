package fr.kotmine.persistanceapi.managers.message

import fr.kotmine.persistanceapi.models.message.ScheduledMessageBean
import javax.sql.DataSource
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.util.ArrayList

object MessageManager {
    // Defines
    private var connection: Connection? = null
    private var statement: PreparedStatement? = null
    private var resultSet: ResultSet? = null

    // Get the scheduled message by ID
    @Throws(Exception::class)
    fun getScheduledMessage(messageId: Int, dataSource: DataSource): ScheduledMessageBean {
        try {
            // Defines
            var scheduledMessageBean: ScheduledMessageBean? = null

            // Set connection
            connection = dataSource.connection

            // Query construction
            val sql = "select message_text, schedule_time from scheduled_messages where message_id = ?"

            statement = connection?.prepareStatement(sql)
            statement?.setInt(1, messageId)

            // Execute the query
            resultSet = statement?.executeQuery()

            // Manage the result in a bean
            if (resultSet?.next() == true) {
                // There's a result
                val messageText = resultSet?.getString("message_text") ?: "null"
                val scheduleTime = resultSet?.getInt("schedule_time") ?: 0

                scheduledMessageBean = ScheduledMessageBean(messageId, messageText, scheduleTime)
            }

            return scheduledMessageBean!!
        } catch (exception: Exception) {
            exception.printStackTrace()
            throw exception
        } finally {
            // Close the query environment in order to prevent leaks
            close()
        }
    }

    // Get the scheduled messages
    @Throws(Exception::class)
    fun getScheduledMessages(dataSource: DataSource): List<ScheduledMessageBean> {
        try {
            // Defines
            val scheduledMessages: MutableList<ScheduledMessageBean> = ArrayList()

            // Set connection
            connection = dataSource.connection

            // Query construction
            val sql = "select message_id, message_text, schedule_time from scheduled_messages"

            statement = connection?.prepareStatement(sql)

            // Execute the query
            resultSet = statement?.executeQuery()

            // Manage the result in a bean
            while (resultSet?.next() == true) {
                // There's a result
                val messageId = resultSet?.getInt("message_id") ?: 0
                val messageText = resultSet?.getString("message_text") ?: "null"
                val scheduleTime = resultSet?.getInt("schedule_time") ?: 0

                scheduledMessages.add(ScheduledMessageBean(messageId, messageText, scheduleTime))
            }

            return scheduledMessages
        } catch (exception: Exception) {
            exception.printStackTrace()
            throw exception
        } finally {
            // Close the query environment in order to prevent leaks
            close()
        }
    }

    // Update the scheduled message
    @Throws(Exception::class)
    fun updateScheduledMessage(scheduledMessageBean: ScheduledMessageBean, dataSource: DataSource) {
        // Update the players data
        try {
            // Set connection
            connection = dataSource.connection

            // Query construction
            val sql = "update scheduled_messages set message_text = ?, schedule_time = ? where message_id = ?"

            statement = connection?.prepareStatement(sql)
            statement?.setString(1, scheduledMessageBean.messageText)
            statement?.setInt(2, scheduledMessageBean.scheduleTime)
            statement?.setInt(3, scheduledMessageBean.messageId)

            // Execute the query
            statement?.executeUpdate()
        } catch (exception: Exception) {
            exception.printStackTrace()
            throw exception
        } finally {
            // Close the query environment in order to prevent leaks
            close()
        }
    }

    // Create the scheduled message
    @Throws(Exception::class)
    fun createScheduledMessage(scheduledMessageBean: ScheduledMessageBean, dataSource: DataSource) {
        // Create the player
        try {
            // Set connection
            connection = dataSource.connection

            // Query construction
            val sql = "insert into scheduled_messages (message_text, schedule_time) values (?, ?)"

            statement = connection?.prepareStatement(sql)
            statement?.setString(1, scheduledMessageBean.messageText)
            statement?.setInt(2, scheduledMessageBean.scheduleTime)

            // Execute the query
            statement?.executeUpdate()
        } catch (exception: Exception) {
            exception.printStackTrace()
            throw exception
        } finally {
            // Close the query environment in order to prevent leaks
            close()
        }
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

