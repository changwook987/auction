package io.github.changwook987.auction

import java.sql.Connection
import java.sql.DriverManager

object Database {


    //MySql configuration
    private lateinit var host: String
    private lateinit var port: String
    private lateinit var database: String
    private lateinit var user: String
    private lateinit var password: String

    fun init(
        host: String,
        port: String,
        database: String,
        user: String,
        password: String,
    ) {
        this.host = host
        this.port = port
        this.database = database
        this.user = user
        this.password = password
    }

    fun getConnection(): Connection {
        Class.forName("com.mysql.cj.jdbc.Driver")

        val url = "jdbc:mysql://$host:$port/$database"
        val connection = DriverManager.getConnection(url, user, password)

        requireNotNull(connection)
        return connection
    }

    inline fun connection(block: Connect.() -> Unit) {
        val conn = getConnection()
        Connect(conn).block()
        conn.close()
    }

    class Connect(val connection: Connection)
}