package io.github.changwook987.auction.plugin

import io.github.changwook987.auction.command.KommandAuction
import io.github.monun.kommand.kommand
import org.bukkit.plugin.java.JavaPlugin

@Suppress("UNUSED")
class AuctionPlugin : JavaPlugin() {
    override fun onEnable() {
        kommand { KommandAuction.register(this) }
    }
}