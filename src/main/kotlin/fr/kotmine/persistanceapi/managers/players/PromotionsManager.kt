package fr.kotmine.persistanceapi.managers.players

import fr.kotmine.persistanceapi.models.shop.PromotionsBean
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.util.ArrayList
import javax.sql.DataSource

object PromotionsManager {
    private var connection: Connection? = null
    private var statement: PreparedStatement? = null
    private var resultSet: ResultSet? = null

    @Throws(Exception::class)
    fun getAllActivePromotions(dataSource: DataSource): List<PromotionsBean> {
        try {
            connection = dataSource.connection
            val promotionList: MutableList<PromotionsBean> = ArrayList()
            val sql = "select promotion_id, type_id, game, multiplier, message, start_date, end_date from promotions where end_date > now()"
            statement = connection!!.prepareStatement(sql)
            resultSet = statement!!.executeQuery()
            while (resultSet!!.next()) {
                val promotionId = resultSet!!.getLong("promotion_id")
                val typePromotion = resultSet!!.getInt("type_id")
                val game = resultSet!!.getInt("game")
                val multiplier = resultSet!!.getInt("multiplier")
                val message = resultSet!!.getString("message")
                val startDate = resultSet!!.getTimestamp("start_date")
                val endDate = resultSet!!.getTimestamp("end_date")
                val promotionsBean = PromotionsBean(promotionId, typePromotion, game, multiplier, message, startDate, endDate)
                promotionList.add(promotionsBean)
            }
            return promotionList
        } catch (exception: Exception) {
            exception.printStackTrace()
            throw exception
        } finally {
            close()
        }
    }

    @Throws(Exception::class)
    fun getPromotion(dataSource: DataSource, typeId: Int, game: Int): List<PromotionsBean> {
        try {
            connection = dataSource.connection
            val promotionList: MutableList<PromotionsBean> = ArrayList()
            var sql = "select promotion_id, type_id, game, multiplier, message, start_date, end_date from promotions where end_date > now()"
            sql += " and type_id = ? and game = ?"
            statement = connection!!.prepareStatement(sql)
            statement!!.setInt(1, typeId)
            statement!!.setInt(2, game)
            resultSet = statement!!.executeQuery()
            while (resultSet!!.next()) {
                val promotionId = resultSet!!.getLong("promotion_id")
                val multiplier = resultSet!!.getInt("multiplier")
                val message = resultSet!!.getString("message")
                val startDate = resultSet!!.getTimestamp("start_date")
                val endDate = resultSet!!.getTimestamp("end_date")
                val promotionsBean = PromotionsBean(promotionId, typeId, game, multiplier, message, startDate, endDate)
                promotionList.add(promotionsBean)
            }
            return promotionList
        } catch (exception: Exception) {
            exception.printStackTrace()
            throw exception
        } finally {
            close()
        }
    }

    @Throws(Exception::class)
    fun createPromotion(promotionsBean: PromotionsBean, dataSource: DataSource) {
        try {
            connection = dataSource.connection
            val sql = "insert into promotions (type_id, game, multiplier, message, start_date, end_date) values (?, ?, ?, ?, ?, ?)"
            statement = connection!!.prepareStatement(sql)
            statement!!.setInt(1, promotionsBean.promotionType)
            statement!!.setInt(2, promotionsBean.game)
            statement!!.setInt(3, promotionsBean.multiplier)
            statement!!.setString(4, promotionsBean.message)
            statement!!.setString(5, promotionsBean.startDate.toString())
            statement!!.setString(6, promotionsBean.endDate.toString())
            statement!!.executeUpdate()
        } catch (exception: Exception) {
            exception.printStackTrace()
            throw exception
        } finally {
            close()
        }
    }

    @Throws(Exception::class)
    fun deletePromotion(promotionsBean: PromotionsBean, dataSource: DataSource) {
        try {
            connection = dataSource.connection
            val sql = "delete from promotions where promotion_id = ?"
            statement = connection!!.prepareStatement(sql)
            statement!!.setLong(1, promotionsBean.promotionId)
            statement!!.executeUpdate(sql)
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
