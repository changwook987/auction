package io.github.changwook987.auction.command

import io.github.changwook987.auction.AuctionItem
import io.github.changwook987.auction.Database
import io.github.changwook987.auction.invfx.InvAuction
import io.github.monun.invfx.openFrame
import io.github.monun.kommand.PluginKommand
import org.bukkit.inventory.ItemStack
import java.util.*
import javax.sql.rowset.serial.SerialBlob

object AuctionCommand {
    fun register(kommand: PluginKommand) {
        kommand.register("auction") {
            then("show") {
                requires { isPlayer }
                executes {
                    player.openFrame(InvAuction.create(player, loadItems()))
                }

                then("my_item") {
                    executes {
                        player.openFrame(InvAuction.create(player, loadItems("where owner = \"${player.uniqueId}\"")))
                    }
                }
            }
            then("sell") {
                requires { isPlayer }
                then("prise" to int()) {
                    executes {
                        val item = player.inventory.itemInMainHand

                        if (item.type.isEmpty) return@executes

                        Database.connection {
                            connection.prepareStatement("insert into item(itemStack, owner, prise) value(?, ?, ?)")
                                .let { pstmt ->
                                    pstmt.setBlob(1, SerialBlob(item.serializeAsBytes()))
                                    pstmt.setString(2, player.uniqueId.toString())
                                    pstmt.setInt(3, it["prise"])

                                    pstmt.executeUpdate()
                                    pstmt.close()
                                }
                        }

                        item.amount = 0
                    }
                }
            }
        }
    }

    private fun loadItems(where: String = ""): List<AuctionItem> {
        val itemList = ArrayList<AuctionItem>()


        Database.connection {
            val statement = connection.createStatement()
            val resultSet = statement.executeQuery("select item_key, itemStack, owner, prise from item $where")

            while (resultSet.next()) {
                itemList += AuctionItem(resultSet.getInt("item_key"),
                    ItemStack.deserializeBytes(resultSet.getBlob("itemStack").run { getBytes(1, length().toInt()) }),
                    resultSet.getInt("prise"),
                    UUID.fromString(resultSet.getString("owner")))
            }
        }

        return itemList
    }
}