package fr.kotmine.persistanceapi.managers

import org.apache.commons.dbcp2.BasicDataSource
import javax.sql.DataSource

class DatabaseManager (
    private val url: String,
    private val name: String,
    private val password: String,
    private val minPoolSize: Int,
    private val maxPoolSize: Int
) {
    var dataSource: DataSource? = null
        private set

    init {
        setupDataSource()
    }

    companion object {
        @Volatile
        private var instance: DatabaseManager? = null

        @JvmStatic
        fun getInstance(url: String, name: String, password: String, minPoolSize: Int, maxPoolSize: Int): DatabaseManager {
            return instance ?: synchronized(this) {
                instance ?: DatabaseManager(url, name, password, minPoolSize, maxPoolSize).also { instance = it }
            }
        }
    }

    private fun setupDataSource() {
        val basicDataSource = BasicDataSource()
        basicDataSource.driverClassName = "com.mysql.jdbc.Driver"
        basicDataSource.url = url
        basicDataSource.username = name
        basicDataSource.password = password
        basicDataSource.initialSize = minPoolSize
        basicDataSource.maxTotal = maxPoolSize
        dataSource = basicDataSource
    }

    fun getDataSource(): DataSource? {
        return dataSource
    }

    fun getSourcesStats(dataSource: DataSource) {
        val basicDataSource = dataSource as? BasicDataSource
        basicDataSource?.let {
            println("Number of active: ${it.numActive}")
            println("Number of idle: ${it.numIdle}")
            println("================================================================================")
        }
    }

    @Throws(Exception::class)
    fun shutdownDataSource(dataSource: DataSource) {
        val basicDataSource = dataSource as? BasicDataSource
        basicDataSource?.close()
    }
}
