package fr.kotmine.persistanceapi.managers.players

import fr.kotmine.persistanceapi.models.players.FriendshipBean
import fr.kotmine.persistanceapi.models.players.PlayerBean
import fr.kotmine.persistanceapi.utils.Transcoder
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.util.*
import javax.sql.DataSource

object FriendshipManager {
    private var connection: Connection? = null
    private var statement: PreparedStatement? = null
    private var resultSet: ResultSet? = null

    @Throws(Exception::class)
    fun postFriendshipDemand(friendship: FriendshipBean, dataSource: DataSource) {
        try {
            connection = dataSource.connection
            val sql = "insert into friendship (requester_uuid, recipient_uuid, demand_date, active_status) values (UNHEX(?), UNHEX(?), now(), false)"
            statement = connection?.prepareStatement(sql)
            statement?.setString(1, Transcoder.encode(friendship.requesterUUID.toString()))
            statement?.setString(2, Transcoder.encode(friendship.recipientUUID.toString()))
            statement?.executeUpdate()
        } catch (exception: Exception) {
            exception.printStackTrace()
            throw exception
        } finally {
            close()
        }
    }

    @Throws(Exception::class)
    fun acceptFriendshipDemand(friendship: FriendshipBean, dataSource: DataSource) {
        try {
            connection = dataSource.connection
            val sql = "update friendship set active_status = true, acceptation_date = now() where friendship_id = ?"
            statement = connection?.prepareStatement(sql)
            statement?.setLong(1, friendship.friendshipId)
            statement?.executeUpdate()
        } catch (exception: Exception) {
            exception.printStackTrace()
            throw exception
        } finally {
            close()
        }
    }

    @Throws(Exception::class)
    fun refuseFriendshipDemand(friendship: FriendshipBean, dataSource: DataSource) {
        try {
            connection = dataSource.connection
            val sql = "delete from friendship where friendship_id = ?"
            statement = connection?.prepareStatement(sql)
            statement?.setLong(1, friendship.friendshipId)
            statement?.executeUpdate()
        } catch (exception: Exception) {
            exception.printStackTrace()
            throw exception
        } finally {
            close()
        }
    }

    @Throws(Exception::class)
    fun getFriendshipDemandList(player: PlayerBean, dataSource: DataSource): List<FriendshipBean> {
        try {
            connection = dataSource.connection
            val friendshipList: MutableList<FriendshipBean> = ArrayList()
            val sql =
                "select friendship_id, HEX(requester_uuid) as requester, HEX(recipient_uuid) as recipient, demand_date, acceptation_date, active_status" +
                        " from friendship where (recipient_uuid = UNHEX(?) or requester_uuid = UNHEX(?)) and active_status = false"
            statement = connection?.prepareStatement(sql)
            statement?.setString(1, Transcoder.encode(player.uuid.toString()))
            statement?.setString(2, Transcoder.encode(player.uuid.toString()))
            resultSet = statement?.executeQuery()
            while (resultSet?.next() == true) {
                val friendshipId = resultSet?.getLong("friendship_id") ?: 0
                val requester = Transcoder.decode(resultSet!!.getString("requester"))
                val requesterUuid = UUID.fromString(requester)
                val recipient = Transcoder.decode(resultSet!!.getString("recipient"))
                val recipientUuid = UUID.fromString(recipient)
                val demandDate = resultSet?.getTimestamp("demand_date")
                val acceptationDate = resultSet?.getTimestamp("acceptation_date")
                val activeStatus = resultSet?.getBoolean("active_status") ?: false
                val friendshipBean = FriendshipBean(
                    friendshipId,
                    requesterUuid,
                    recipientUuid,
                    demandDate!!,
                    acceptationDate,
                    activeStatus
                )
                friendshipList.add(friendshipBean)
            }
            return friendshipList
        } catch (exception: Exception) {
            exception.printStackTrace()
            throw exception
        } finally {
            close()
        }
    }

    @Throws(Exception::class)
    fun getFriendshipList(player: PlayerBean, dataSource: DataSource): List<FriendshipBean> {
        try {
            connection = dataSource.connection
            val friendshipList: MutableList<FriendshipBean> = ArrayList()
            val sql =
                "select friendship_id, HEX(requester_uuid) as requester, HEX(recipient_uuid) as recipient, demand_date, acceptation_date, active_status" +
                        " from friendship where (recipient_uuid = UNHEX(?) or requester_uuid = UNHEX(?)) and active_status=true"
            statement = connection?.prepareStatement(sql)
            statement?.setString(1, Transcoder.encode(player.uuid.toString()))
            statement?.setString(2, Transcoder.encode(player.uuid.toString()))
            resultSet = statement?.executeQuery()
            while (resultSet?.next() == true) {
                val friendshipId = resultSet?.getLong("friendship_id") ?: 0
                val requester = Transcoder.decode(resultSet?.getString("requester")!!)
                val requesterUuid = UUID.fromString(requester)
                val recipient = Transcoder.decode(resultSet?.getString("recipient")!!)
                val recipientUuid = UUID.fromString(recipient)
                val demandDate = resultSet?.getTimestamp("demand_date")
                val acceptationDate = resultSet?.getTimestamp("acceptation_date")
                val activeStatus = resultSet?.getBoolean("active_status") ?: false
                val friendshipBean = FriendshipBean(
                    friendshipId,
                    requesterUuid,
                    recipientUuid,
                    demandDate!!,
                    acceptationDate,
                    activeStatus
                )
                friendshipList.add(friendshipBean)
            }
            return friendshipList
        } catch (exception: Exception) {
            exception.printStackTrace()
            throw exception
        } finally {
            close()
        }
    }

    @Throws(Exception::class)
    fun getFriendshipNamedList(requester: PlayerBean, recipient: PlayerBean, dataSource: DataSource): FriendshipBean {
        try {
            connection = dataSource.connection
            var friendshipBean: FriendshipBean? = null
            val sql =
                "select friendship_id, HEX(requester_uuid) as requester, HEX(recipient_uuid) as recipient, demand_date, acceptation_date, active_status" +
                        " from friendship where recipient_uuid = UNHEX(?) and requester_uuid = UNHEX(?)"
            statement = connection?.prepareStatement(sql)
            statement?.setString(1, Transcoder.encode(recipient.uuid.toString()))
            statement?.setString(2, Transcoder.encode(requester.uuid.toString()))
            resultSet = statement?.executeQuery()
            if (resultSet?.next() == true) {
                val friendshipId = resultSet?.getLong("friendship_id") ?: 0
                val requesterName = Transcoder.decode(resultSet?.getString("requester")!!)
                val requesterUuid = UUID.fromString(requesterName)
                val recipientName = Transcoder.decode(resultSet?.getString("recipient")!!)
                val recipientUuid = UUID.fromString(recipientName)
                val demandDate = resultSet?.getTimestamp("demand_date")
                val acceptationDate = resultSet?.getTimestamp("acceptation_date")
                val activeStatus = resultSet?.getBoolean("active_status") ?: false
                friendshipBean = FriendshipBean(
                    friendshipId,
                    requesterUuid,
                    recipientUuid,
                    demandDate!!,
                    acceptationDate,
                    activeStatus
                )
            }
            return friendshipBean!!
        } catch (exception: Exception) {
            exception.printStackTrace()
            throw exception
        } finally {
            close()
        }
    }

    private fun close() {
        try {
            resultSet?.close()
            statement?.close()
            connection?.close()
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }
}
