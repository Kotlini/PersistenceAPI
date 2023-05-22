package fr.kotmine.persistanceapi.managers.statistics

import fr.kotmine.persistanceapi.models.statistics.HostStatisticsBean
import fr.kotmine.persistanceapi.utils.Transcoder
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Timestamp
import javax.sql.DataSource

object HostStatisticsManager {
    private var connection: Connection? = null
    private var statement: PreparedStatement? = null
    private var resultSet: ResultSet? = null

    @Throws(Exception::class)
    fun createHostRecord(hostStatisticsBean: HostStatisticsBean, dataSource: DataSource) {
        try {
            connection = dataSource.connection
            val sql = "insert into host_stats (template_id, host_id, ip_address, player_uuid, started_time, played_time) values (?, ?, ?, UNHEX(?), ?, ?)"
            statement = connection?.prepareStatement(sql)
            statement?.setString(1, hostStatisticsBean.templateId)
            statement?.setString(2, hostStatisticsBean.hostId)
            statement?.setString(3, hostStatisticsBean.ipAddress)
            statement?.setString(4, Transcoder.encode(hostStatisticsBean.playerUuid.toString()))
            statement?.setString(5, Timestamp(hostStatisticsBean.startedTime).toString())
            statement?.setLong(6, hostStatisticsBean.playedTime)
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
