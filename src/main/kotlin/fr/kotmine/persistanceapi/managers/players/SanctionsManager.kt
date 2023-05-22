package fr.kotmine.persistanceapi.managers.players

import fr.kotmine.persistanceapi.models.players.PlayerBean
import fr.kotmine.persistanceapi.models.players.SanctionBean
import fr.kotmine.persistanceapi.utils.Transcoder
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Timestamp
import java.util.*
import javax.sql.DataSource

object SanctionManager {
    // Defines
    private var connection: Connection? = null
    private var statement: PreparedStatement? = null
    private var resultSet: ResultSet? = null

    // Create a sanction
    @Throws(Exception::class)
    fun applySanction(sanctionType: Int, sanction: SanctionBean, dataSource: DataSource) {
        // Create the sanction
        try {
            // Set connection
            connection = dataSource.connection

            val expirationTime = sanction.expirationTime
            var expirationDate = "0000-00-00 00:00:00"

            if (expirationTime != null)
                expirationDate = expirationTime.toString()

            // Query construction
            var sql = "insert into sanctions (player_uuid, type_id, reason, punisher_uuid, expiration_date, is_deleted, creation_date, update_date)"
            sql += " values (UNHEX(?), ?, ?, UNHEX(?), ?, 0, now(), now())"

            statement = connection!!.prepareStatement(sql)
            statement?.setString(1, Transcoder.encode(sanction.playerUuid.toString()))
            statement?.setInt(2, sanctionType)
            statement?.setString(3, sanction.reason)
            statement?.setString(4, Transcoder.encode(sanction.punisherUuid.toString()))
            statement?.setString(5, expirationDate)

            // Execute the query
            statement!!.executeUpdate()
        } catch (exception: Exception) {
            exception.printStackTrace()
            throw exception
        } finally {
            // Close the query environment in order to prevent leaks
        this.close()
        }
    }

    // Remove a sanction
    @Throws(Exception::class)
    fun removeSanction(sanctionType: Int, player: PlayerBean, dataSource: DataSource) {
        // Remove the last active sanction
        try {
            // Set connection
            connection = dataSource.connection

            // Query construction
            var sql =
                "update sanctions set is_deleted=1, update_date = now() where type_id = ? and player_uuid = UNHEX(?)"
            sql += " and is_deleted = 0 order by creation_date desc limit 1"

            statement = connection?.prepareStatement(sql)
            statement?.setInt(1, sanctionType)
            statement?.setString(2, Transcoder.encode(player.uuid.toString()))

            // Execute the query
            statement?.executeUpdate()
        } catch (exception: Exception) {
            exception.printStackTrace()
            throw exception
        } finally {
            // Close the query environment in order to prevent leaks
        this.close()
        }
    }
    fun getPlayerBanned(player: PlayerBean, dataSource: DataSource): SanctionBean? {
        // Defines
        var sanction: SanctionBean? = null

        // Do the check of ban
        try {
            // Set connection
            val connection = dataSource.connection
            val expirationTime: Timestamp?

            // Query construction
            var sql = ""
            sql += "select sanction_id, HEX(player_uuid) as uuid , type_id, reason, HEX(punisher_uuid) as punisher, expiration_date, is_deleted, creation_date, update_date from sanctions"
            sql += " where player_uuid=UNHEX(?) and type_id = ? and (expiration_date > now() or expiration_date= '0000-00-00 00:00:00') and is_deleted = 0"

            val statement = connection.prepareStatement(sql)
            statement.setString(1, Transcoder.encode(player.uuid.toString()))
            statement.setInt(2, SanctionBean.BAN)

            // Execute the query
            val resultset = statement.executeQuery()

            // Manage the result
            if (resultset.next()) {
                // The player is banned
                val sanctionId = resultset.getLong("sanction_id")
                val banPlayer = Transcoder.decode(resultset.getString("uuid"))
                val playerUuid = UUID.fromString(banPlayer)
                val typeId = resultset.getInt("type_id")
                val reason = resultset.getString("reason")
                val punisher = Transcoder.decode(resultset.getString("punisher"))
                val punisherUuid = UUID.fromString(punisher)

                expirationTime = try {
                    resultset.getTimestamp("expiration_date")
                } catch (dateException: Exception) {
                    null
                }

                val isDeleted = resultset.getBoolean("is_deleted")
                val creationDate = resultset.getTimestamp("creation_date")
                val updateDate = resultset.getTimestamp("update_date")
                sanction = SanctionBean(
                    sanctionId,
                    playerUuid,
                    typeId,
                    reason,
                    punisherUuid,
                    expirationTime,
                    isDeleted,
                    creationDate,
                    updateDate
                )
                return sanction
            } else {
                // The player is not banned
                return null
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
            throw exception
        } finally {
            // Close the query environment in order to prevent leaks
            this.close()
        }
    }

    // Check if a player is muted
    @Throws(Exception::class)
    fun getPlayerMuted(player: PlayerBean, dataSource: DataSource): SanctionBean? {
        var sanction: SanctionBean? = null

        // Do the check of mute
        try {
            // Set connection
            connection = dataSource.connection
            val expirationTime: Timestamp?

            // Query construction
            var sql = "select sanction_id, HEX(player_uuid) as uuid , type_id, reason, HEX(punisher_uuid) as punisher, expiration_date, is_deleted, creation_date, update_date from sanctions"
            sql += " where player_uuid = UNHEX(?) and type_id = ? and expiration_date > now() and is_deleted = 0"

            statement = connection!!.prepareStatement(sql)
            statement!!.setString(1, Transcoder.encode(player.uuid.toString()))
            statement!!.setInt(2, SanctionBean.MUTE)

            // Execute the query
            resultSet = statement!!.executeQuery()

            // Manage the result
            if (resultSet!!.next()) {
                // The player is muted
                val sanctionId = resultSet!!.getLong("sanction_id")
                val mutePlayer = Transcoder.decode(resultSet!!.getString("uuid"))
                val playerUuid = UUID.fromString(mutePlayer)
                val typeId = resultSet!!.getInt("type_id")
                val reason = resultSet!!.getString("reason")
                val punisher = Transcoder.decode(resultSet!!.getString("punisher"))
                val punisherUuid = UUID.fromString(punisher)

                expirationTime = try {
                    resultSet!!.getTimestamp("expiration_date")
                } catch (dateException: Exception) {
                    null
                }

                val isDeleted = resultSet!!.getBoolean("is_deleted")
                val creationDate = resultSet!!.getTimestamp("creation_date")
                val updateDate = resultSet!!.getTimestamp("update_date")
                sanction = SanctionBean(sanctionId, playerUuid, typeId, reason, punisherUuid, expirationTime, isDeleted, creationDate, updateDate)
                return sanction
            } else {
                // The player is not banned
                return null
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
            throw exception
        } finally {
            // Close the query environment in order to prevent leaks
            close()
        }
    }

    // Get all active sanctions by type
    @Throws(Exception::class)
    fun getAllSanctions(uuid: UUID, sanctionType: Int, dataSource: DataSource): List<SanctionBean> {
        // Get all sanctions
        try {
            // Set connection
            connection = dataSource.connection
            val sanctionList: MutableList<SanctionBean> = ArrayList()
            var expirationTime: Timestamp?

            // Query construction
            var sql = "select sanction_id, HEX(player_uuid) as player_uuid, type_id, reason, HEX(punisher_uuid) as punisher_uuid, expiration_date, is_deleted, creation_date, update_date from sanctions"
            sql += " where player_uuid=UNHEX(?) and type_id = ? order by creation_date desc"

            statement = connection!!.prepareStatement(sql)
            statement!!.setString(1, Transcoder.encode(uuid.toString()))
            statement!!.setInt(2, sanctionType)

            // Execute the query
            resultSet = statement!!.executeQuery()

            // Manage the result in a bean
            while (resultSet!!.next()) {
                // There's a result
                val sanctionId = resultSet!!.getLong("sanction_id")
                val playerUuid = Transcoder.decode(resultSet!!.getString("player_uuid"))
                val typeId = resultSet!!.getInt("type_id")
                val reason = resultSet!!.getString("reason")
                val punisherUUID = Transcoder.decode(resultSet!!.getString("punisher_uuid"))

                expirationTime = try {
                    resultSet!!.getTimestamp("expiration_date")
                } catch (dateException: Exception) {
                    null
                }

                val isDeleted = resultSet!!.getBoolean("is_deleted")
                val creationDate = resultSet!!.getTimestamp("creation_date")
                val updateDate = resultSet!!.getTimestamp("update_date")
                val sanction = SanctionBean(sanctionId, UUID.fromString(playerUuid), typeId, reason, UUID.fromString(punisherUUID), expirationTime, isDeleted, creationDate, updateDate)
                sanctionList.add(sanction)
            }
            return sanctionList
        } catch (exception: Exception) {
            exception.printStackTrace()
            throw exception
        } finally {
            // Close the query environment in order to prevent leaks
            close()
        }
    }

    @Throws(Exception::class)
    fun getAllActiveSanctions(uuid: UUID, sanctionType: Int, dataSource: DataSource): List<SanctionBean> {
        // Get all sanctions
        try {
            // Set connection
            connection = dataSource.connection
            val sanctionList: MutableList<SanctionBean> = ArrayList()
            var expirationTime: Timestamp?

            // Query construction
            var sql = "select sanction_id, HEX(player_uuid) as player_uuid, type_id, reason, HEX(punisher_uuid) as punisher_uuid, expiration_date, is_deleted, creation_date, update_date from sanctions"
            sql += " where player_uuid = UNHEX(?) and type_id = ? and is_deleted = 0 order by creation_date desc"

            statement = connection!!.prepareStatement(sql)
            statement!!.setString(1, Transcoder.encode(uuid.toString()))
            statement!!.setInt(2, sanctionType)

            // Execute the query
            resultSet = statement!!.executeQuery()

            // Manage the result in a bean
            while (resultSet!!.next()) {
                // There's a result
                val sanctionId = resultSet!!.getLong("sanction_id")
                val playerUuid = Transcoder.decode(resultSet!!.getString("player_uuid"))
                val typeId = resultSet!!.getInt("type_id")
                val reason = resultSet!!.getString("reason")
                val punisherUUID = Transcoder.decode(resultSet!!.getString("punisher_uuid"))

                expirationTime = try {
                    resultSet!!.getTimestamp("expiration_date")
                } catch (dateException: Exception) {
                    null
                }

                val isDeleted = resultSet!!.getBoolean("is_deleted")
                val creationDate = resultSet!!.getTimestamp("creation_date")
                val updateDate = resultSet!!.getTimestamp("update_date")
                val sanction = SanctionBean(
                    sanctionId,
                    UUID.fromString(playerUuid),
                    typeId,
                    reason,
                    UUID.fromString(punisherUUID),
                    expirationTime,
                    isDeleted,
                    creationDate,
                    updateDate
                )
                sanctionList.add(sanction)
            }
            return sanctionList
        } catch (exception: Exception) {
            exception.printStackTrace()
            throw exception
        } finally {
            // Close the query environment in order to prevent leaks
            this.close()
        }
    }

    @Throws(Exception::class)
    fun getAllPassiveSanctions(uuid: UUID, sanctionType: Int, dataSource: DataSource): List<SanctionBean> {
        // Get all sanctions
        try {
            // Set connection
            connection = dataSource.connection
            val sanctionList: MutableList<SanctionBean> = ArrayList()
            var expirationTime: Timestamp?

            // Query construction
            var sql = "select sanction_id, (HEX(player_uuid)) as player_uuid, type_id, reason, (HEX(punisher_uuid)) as punisher_uuid, expiration_date, is_deleted, creation_date, update_date from sanctions"
            sql += " where player_uuid = UNHEX(?) and type_id = ? and is_deleted = 1 order by creation_date desc"

            statement = connection!!.prepareStatement(sql)
            statement!!.setString(1, Transcoder.encode(uuid.toString()))
            statement!!.setInt(2, sanctionType)

            // Execute the query
            resultSet = statement!!.executeQuery()

            // Manage the result in a bean
            while (resultSet!!.next()) {
                // There's a result
                val sanctionId = resultSet!!.getLong("sanction_id")
                val playerUuid = Transcoder.decode(resultSet!!.getString("player_uuid"))
                val typeId = resultSet!!.getInt("type_id")
                val reason = resultSet!!.getString("reason")
                val punisherUUID = Transcoder.decode(resultSet!!.getString("punisher_uuid"))

                expirationTime = try {
                    resultSet!!.getTimestamp("expiration_date")
                } catch (dateException: Exception) {
                    null
                }

                val isDeleted = resultSet!!.getBoolean("is_deleted")
                val creationDate = resultSet!!.getTimestamp("creation_date")
                val updateDate = resultSet!!.getTimestamp("update_date")
                val sanction = SanctionBean(
                    sanctionId,
                    UUID.fromString(playerUuid),
                    typeId,
                    reason,
                    UUID.fromString(punisherUUID),
                    expirationTime,
                    isDeleted,
                    creationDate,
                    updateDate
                )
                sanctionList.add(sanction)
            }
            return sanctionList
        } catch (exception: Exception) {
            exception.printStackTrace()
            throw exception
        } finally {
            // Close the query environment in order to prevent leaks
            this.close()
        }
    }

    @Throws(Exception::class)
    fun updateSanctionStatus(sanctionId: Long, status: Boolean, dataSource: DataSource) {
        // Update the sanction status
        try {
            // Set connection
            connection = dataSource.connection

            // Query construction
            val sql = "update sanctions set is_deleted = ?, update_date = now() where sanction_id = ?"

            statement = connection!!.prepareStatement(sql)
            statement!!.setBoolean(1, status)
            statement!!.setLong(2, sanctionId)

            // Execute the query
            statement!!.executeUpdate()
        } catch (exception: Exception) {
            exception.printStackTrace()
            throw exception
        } finally {
            // Close the query environment in order to prevent leaks
            close()
        }
    }

    @Throws(Exception::class)
    fun getAllModeratorSanctions(uuid: UUID, dataSource: DataSource): List<SanctionBean>? {
        // Get all sanctions
        return try {
            // Set connection
            connection = dataSource.connection
            val sanctionList: MutableList<SanctionBean> = ArrayList()
            var expirationTime: Timestamp?

            // Query construction
            var sql = "select sanction_id, HEX(player_uuid) as player_uuid, type_id, reason, HEX(punisher_uuid) as punisher_uuid, expiration_date, is_deleted, creation_date, update_date from sanctions"
            sql += " where punisher_uuid = UNHEX(?) order by creation_date desc"
            statement = connection?.prepareStatement(sql)
            statement!!.setString(1, Transcoder.encode(uuid.toString()))

            // Execute the query
            resultSet = statement!!.executeQuery()

            // Manage the result in a bean
            while (resultSet!!.next()) {
                // There's a result
                val sanctionId: Long = resultSet!!.getLong("sanction_id")
                val playerUuid = Transcoder.decode(resultSet!!.getString("player_uuid"))
                val typeId: Int = resultSet!!.getInt("type_id")
                val reason: String = resultSet!!.getString("reason")
                val punisherUUID = Transcoder.decode(resultSet!!.getString("punisher_uuid"))
                expirationTime = try {
                    resultSet!!.getTimestamp("expiration_date")
                } catch (dateException: Exception) {
                    null
                }
                val isDeleted: Boolean = resultSet!!.getBoolean("is_deleted")
                val creationDate: Timestamp = resultSet!!.getTimestamp("creation_date")
                val updateDate: Timestamp = resultSet!!.getTimestamp("update_date")
                val sanction = SanctionBean(
                    sanctionId,
                    UUID.fromString(playerUuid),
                    typeId,
                    reason,
                    UUID.fromString(punisherUUID),
                    expirationTime,
                    isDeleted,
                    creationDate,
                    updateDate
                )
                sanctionList.add(sanction)
            }
            sanctionList
        } catch (exception: Exception) {
            exception.printStackTrace()
            throw exception
        } finally {
            // Close the query environment in order to prevent leaks
            close()
        }
    }

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