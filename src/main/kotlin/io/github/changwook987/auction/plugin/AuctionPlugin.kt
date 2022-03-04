package io.github.changwook987.auction.plugin

import io.github.changwook987.auction.Database
import io.github.changwook987.auction.command.AuctionCommand
import io.github.monun.kommand.kommand
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class AuctionPlugin : JavaPlugin() {
    override fun onEnable() {
        val yaml = YamlConfiguration.loadConfiguration(File(dataFolder, "databaseConfig.yml"))

        val host = yaml.getString("host")
        val port = yaml.getString("port")
        val database = yaml.getString("database")
        val user = yaml.getString("user")
        val password = yaml.getString("password")

        if (listOf(host, port, database, user, password).any { it == null }) {
            logger.warning("please write databaseConfig.yml")

            yaml.set("host", "")
            yaml.set("port", "")
            yaml.set("database", "")
            yaml.set("user", "")
            yaml.set("password", "")

            yaml.save(File(dataFolder, "databaseConfig.yml"))
            server.shutdown()
            return
        }

        Database.init(host!!, port!!, database!!, user!!, password!!)

        Database.connection {
            prepareStatement("create table if not exists item(item_key int primary key auto_increment, itemStack blob, owner char(36), prise int)") {
                executeUpdate()
            }

            prepareStatement("create table if not exists user(user_key int primary key auto_increment, uuid char(36), money int)") {
                executeUpdate()
            }

            prepareStatement("create table if not exists shopping_bag(entry_key int primary key auto_increment, user_key int, item_key int, unique key(user_key, item_key) )") {
                executeUpdate()
            }
        }

        EventListener(this).register()
        kommand { AuctionCommand.register(this) }
    }
}