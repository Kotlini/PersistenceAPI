package fr.kotmine.persistanceapi.managers.players

import fr.kotmine.persistanceapi.models.players.NicknameBean
import javax.sql.DataSource
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet

object NickNameManager {
    // Defines
    private var connection: Connection? = null
    private var statement: PreparedStatement? = null
    private var resultSet: ResultSet? = null

    // Get a random nickname
    @Throws(Exception::class)
    fun getRandomNickname(dataSource: DataSource): NicknameBean? {
        // get a free random nickname
        try {
            // Set connection
            connection = dataSource.connection

            // Query construction
            val sql = "select nick_id, nickname, blacklisted, used from nickname where blacklisted = 0 and used = 0 order by rand() limit 1"

            statement = connection?.prepareStatement(sql)

            // Execute the query
            resultSet = statement?.executeQuery()

            // Manage the result in a bean
           if (resultSet?.next() == true) {
                // There's a result
                val nickId = resultSet?.getLong("nick_id") ?: 0
                val nickname = resultSet?.getString("nickname") ?: "null"
                val blacklisted = resultSet?.getBoolean("blacklisted") ?: false
                val used = resultSet?.getBoolean("used") ?: false
                val nicknameBean = NicknameBean(nickId, nickname, blacklisted, used)
                this.close()
                reserveNickname(nicknameBean.nickId, dataSource)
                return nicknameBean
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
            throw exception
        } finally {
            // Close the query environment in order to prevent leaks
            this.close()
        }
        return null
    }

    // Check if a nickname is blacklisted
    @Throws(Exception::class)
    fun isNicknameBlacklisted(nickname: String, dataSource: DataSource): Boolean {
        try {
            // Set connection
            connection = dataSource.connection

            // Query construction
            val sql = "select nick_id from nickname where nickname = ? and blacklisted = 1"

            statement = connection?.prepareStatement(sql)
            statement?.setString(1, nickname)

            // Execute the query
            resultSet = statement?.executeQuery()

            // If there'a a result
            return resultSet?.next() ?: false
        } catch (exception: Exception) {
            exception.printStackTrace()
            throw exception
        } finally {
            // Close the query environment in order to prevent leaks
            this.close()
        }
    }

    // Reserve a nickname
    @Throws(Exception::class)
    private fun reserveNickname(nickId: Long, dataSource: DataSource) {
        try {
            // Set connection
            connection = dataSource.connection

            // Query construction
            val sql = "update nickname set used = true where nick_id = ?"

            statement = connection?.prepareStatement(sql)
            statement?.setLong(1, nickId)

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

    // Free a nickname
    @Throws(Exception::class)
    fun freeNickname(nickname: String, dataSource: DataSource) {
        try {
            // Set connection
            connection = dataSource.connection

            // Query construction
            val sql = "update nickname set used = false where nickname = ?"

            statement = connection?.prepareStatement(sql)
            statement?.setString(1, nickname)

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

    // Close the connection
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
