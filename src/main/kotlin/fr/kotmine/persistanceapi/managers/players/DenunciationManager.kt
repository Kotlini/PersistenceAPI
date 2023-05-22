package fr.kotmine.persistanceapi.managers.players

import fr.kotmine.persistanceapi.models.players.DenunciationBean
import fr.kotmine.persistanceapi.models.players.PlayerBean
import fr.kotmine.persistanceapi.utils.Transcoder
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import javax.sql.DataSource

object DenunciationManager {
    private var connection: Connection? = null
    private var statement: PreparedStatement? = null
    private var resultSet: ResultSet? = null

    fun denouncePlayer(player: PlayerBean, denunciation: DenunciationBean, dataSource: DataSource) {
        try {
            connection = dataSource.connection

            val sql = "insert into denunciations (denouncer, date, reason, suspect_name) values (UNHEX(?), now(), ?, ?)"

            statement = connection?.prepareStatement(sql)
            statement?.setString(1, Transcoder.encode(player.uuid.toString()))
            statement?.setString(2, denunciation.reason)
            statement?.setString(3, denunciation.suspectName)

            statement?.executeUpdate()
        } catch (exception: Exception) {
            exception.printStackTrace()
            throw exception
        } finally {
            close()
        }
    }

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

