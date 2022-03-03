package io.github.changwook987.auction.plugin

import io.github.changwook987.auction.Database
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.java.JavaPlugin

class EventListener(private val plugin: JavaPlugin) : Listener {
    fun register() {
        plugin.server.pluginManager.registerEvents(this, plugin)
    }

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        val player = event.player

        Database.connection {
            connection.createStatement().let { statement ->
                statement.executeQuery("select count(*) from user where uuid = \"${player.uniqueId}\"")
                    .let { resultSet ->
                        if (resultSet.next()) {
                            if (resultSet.getInt(1) == 0) {
                                connection.prepareStatement("insert into user(uuid, money) values(\"${player.uniqueId}\", 0)").let {
                                    it.executeUpdate()
                                    it.close()
                                }
                            }
                        }
                        resultSet.close()
                    }
                statement.close()
            }
        }
    }
}