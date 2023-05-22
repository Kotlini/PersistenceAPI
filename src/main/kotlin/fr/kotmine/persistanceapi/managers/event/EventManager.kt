package fr.kotmine.persistanceapi.managers.event

import fr.kotmine.persistanceapi.models.events.EventBean
import fr.kotmine.persistanceapi.models.events.EventWinnerBean
import fr.kotmine.persistanceapi.utils.Transcoder
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Timestamp
import java.util.*
import javax.sql.DataSource

object EventManager {
    // Defines
    private var connection: Connection? = null
    private var statement: PreparedStatement? = null
    private var resultSet: ResultSet? = null

    // Get the event by ID
    @Throws(Exception::class)
    fun getEvent(eventId: Long, dataSource: DataSource): EventBean {
        try {
            // Defines
            var eventBean: EventBean? = null

            // Set connection
            connection = dataSource.connection

            // Query construction
            val sql = "select event_organizer, event_template, reward_coins, reward_pearls, event_date from events where event_id = ?"

            statement = connection?.prepareStatement(sql)
            statement?.setLong(1, eventId)

            // Execute the query
            resultSet = statement?.executeQuery()

            // Manage the result in a bean
            if (resultSet?.next() == true) {
                // There's a result
                val eventOrganizer =
                    UUID.fromString(Transcoder.decode(resultSet!!.getString("event_organizer")))
                val eventTemplate = resultSet!!.getString("event_template")
                val rewardCoins = resultSet!!.getInt("reward_coins")
                val rewardPearls = resultSet!!.getInt("reward_pearls")
                val eventDate = resultSet!!.getTimestamp("event_date")

                eventBean = EventBean(
                    eventId,
                    eventOrganizer,
                    eventTemplate,
                    rewardCoins,
                    rewardPearls,
                    eventDate
                )
            }

            return eventBean!!
        } catch (exception: Exception) {
            exception.printStackTrace()
            throw exception
        } finally {
            // Close the query environment in order to prevent leaks
            close()
        }
    }

    // Get the event by date
    @Throws(Exception::class)
    fun getEvent(eventDate: Timestamp, dataSource: DataSource): EventBean {
        try {
            // Defines
            var eventBean: EventBean? = null

            // Set connection
            connection = dataSource.connection

            // Query construction
            val sql = "select event_id, event_organizer, event_template, reward_coins, reward_pearls from events where event_date = ?"

            statement = connection?.prepareStatement(sql)
            statement?.setString(1, eventDate.toString())

            // Execute the query
            resultSet = statement?.executeQuery()

            // Manage the result in a bean
            if (resultSet?.next() == true) {
                // There's a result
                val eventId = resultSet!!.getLong("event_id")
                val eventOrganizer =
                    UUID.fromString(Transcoder.decode(resultSet!!.getString("event_organizer")))
                val eventTemplate = resultSet!!.getString("event_template")
                val rewardCoins = resultSet!!.getInt("reward_coins")
                val rewardPearls = resultSet!!.getInt("reward_pearls")

                eventBean = EventBean(
                    eventId,
                    eventOrganizer,
                    eventTemplate,
                    rewardCoins,
                    rewardPearls,
                    eventDate
                )
            }

            return eventBean!!
        } catch (exception: Exception) {
            exception.printStackTrace()
            throw exception
        } finally {
            // Close the query environment in order to prevent leaks
            close()
        }
    }

    // Get the winners of an event
    @Throws(Exception::class)
    fun getEventWinners(eventId: Long, dataSource: DataSource): List<EventWinnerBean> {
        try {
            // Defines
            val eventWinners = ArrayList<EventWinnerBean>()

            // Set connection
            connection = dataSource.connection

            // Query construction
            val sql = "select win_id, event_winner from event_winners where event_id = ?"

            statement = connection?.prepareStatement(sql)
            statement?.setLong(1, eventId)

            // Execute the query
            resultSet = statement?.executeQuery()

            // Manage the result in a bean
            while (resultSet?.next() == true) {
                // There's a result
                val winId = resultSet?.getLong("win_id")
                val eventWinner =
                    UUID.fromString(Transcoder.decode(resultSet!!.getString("event_winner")))

                eventWinners.add(EventWinnerBean(winId!!, eventId, eventWinner))
            }

            return eventWinners
        } catch (exception: Exception) {
            exception.printStackTrace()
            throw exception
        } finally {
            // Close the query environment in order to prevent leaks
            close()
        }
    }

    // Get the events
    @Throws(Exception::class)
    fun getEvents(dataSource: DataSource): List<EventBean> {
        try {
            // Defines
            val events = ArrayList<EventBean>()

            // Set connection
            connection = dataSource.connection

            // Query construction
            val sql = "select event_id, event_organizer, event_template, reward_coins, reward_pearls, event_date from events"

            statement = connection?.prepareStatement(sql)

            // Execute the query
            resultSet = statement?.executeQuery()

            // Manage the result in a bean
            while (resultSet?.next() == true) {
                // There's a result
                val eventId = resultSet!!.getLong("event_id")
                val eventOrganizer =
                    UUID.fromString(Transcoder.decode(resultSet!!.getString("event_organizer")))
                val eventTemplate = resultSet!!.getString("event_template")
                val rewardCoins = resultSet!!.getInt("reward_coins")
                val rewardPearls = resultSet!!.getInt("reward_pearls")
                val eventDate = resultSet!!.getTimestamp("event_date")

                events.add(
                    EventBean(
                        eventId,
                        eventOrganizer,
                        eventTemplate,
                        rewardCoins,
                        rewardPearls,
                        eventDate
                    )
                )
            }

            return events
        } catch (exception: Exception) {
            exception.printStackTrace()
            throw exception
        } finally {
            // Close the query environment in order to prevent leaks
            close()
        }
    }

    // Update the event
    @Throws(Exception::class)
    fun updateEvent(eventBean: EventBean, dataSource: DataSource) {
        // Update the players data
        try {
            // Set connection
            connection = dataSource.connection

            val eventDate = eventBean.eventDate
            val eventDateString: String = eventDate.toString()

            // Query construction
            val sql = "update events set event_organizer = ?, event_template = ?, reward_coins = ?, reward_pearls = ?, event_date = ? where event_id = ?"

            statement = connection?.prepareStatement(sql)
            statement?.setString(1, Transcoder.encode(eventBean.eventOrganizer.toString()))
            statement?.setString(2, eventBean.eventTemplate)
            statement?.setInt(3, eventBean.rewardCoins)
            statement?.setInt(4, eventBean.rewardPearls)
            statement?.setString(5, eventDateString)
            statement?.setLong(6, eventBean.eventId)

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

    // Update the event
    @Throws(Exception::class)
    fun updateEventWinner(eventWinnerBean: EventWinnerBean, dataSource: DataSource) {
        // Update the players data
        try {
            // Set connection
            connection = dataSource.connection

            // Query construction
            val sql = "update event_winners set event_id = ?, event_winner = ? where win_id = ?"

            statement = connection?.prepareStatement(sql)
            statement?.setLong(1, eventWinnerBean.eventId)
            statement?.setString(2, Transcoder.encode(eventWinnerBean.eventWinner.toString()))
            statement?.setLong(3, eventWinnerBean.winId)

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

    // Create the event
    @Throws(Exception::class)
    fun createEvent(eventBean: EventBean, dataSource: DataSource) {
        // Create the player
        try {
            // Set connection
            connection = dataSource.connection

            val eventDate = eventBean.eventDate
            var eventDateString = "0000-00-00 00:00:00"

            if (eventDate != null)
                eventDateString = eventDate.toString()

            // Query construction
            val sql =
                "insert into events (event_organizer, event_template, reward_coins, reward_pearls, event_date) values (UNHEX(?), ?, ?, ?, ?)"

            statement = connection?.prepareStatement(sql)
            statement?.setString(1, Transcoder.encode(eventBean.eventOrganizer.toString()))
            statement?.setString(2, eventBean.eventTemplate)
            statement?.setInt(3, eventBean.rewardCoins)
            statement?.setInt(4, eventBean.rewardPearls)
            statement?.setString(5, eventDateString)

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

    // Create the event winner
    @Throws(Exception::class)
    fun createWinnerEvent(eventWinnerBean: EventWinnerBean, dataSource: DataSource) {
        // Create the player
        try {
            // Set connection
            connection = dataSource.connection

            // Query construction
            val sql = "insert into event_winners (event_id, event_winner) values (?, UNHEX(?))"

            statement = connection?.prepareStatement(sql)
            statement?.setLong(1, eventWinnerBean.eventId)
            statement?.setString(2, Transcoder.encode(eventWinnerBean.eventWinner.toString()))

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