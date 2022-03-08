package io.github.changwook987.auction.invfx

import io.github.changwook987.auction.AuctionItem
import io.github.changwook987.auction.AuctionUser
import io.github.changwook987.auction.Database
import io.github.monun.invfx.InvFX
import io.github.monun.invfx.frame.InvFrame
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.ItemStack
import java.util.*

object InvAuction {
    private val previousButton = ItemStack(Material.OAK_SIGN).apply {
        itemMeta = itemMeta.apply {
            displayName(text("previous"))
        }
    }

    private val nextButton = ItemStack(Material.OAK_SIGN).apply {
        itemMeta = itemMeta.apply {
            displayName(text("next"))
        }
    }

    private fun moneyView(player: Player): ItemStack {
        var money = 0

        //Database connection
        Database.connection {
            val statement = connection.createStatement()
            val resultSet = statement.executeQuery("select money from user where uuid = \"${player.uniqueId}\"")

            if (resultSet.next()) {
                money = resultSet.getInt(1)
            }

            resultSet.close()
            statement.close()
        }

        return ItemStack(Material.BUNDLE).apply {
            itemMeta = itemMeta.apply {
                displayName(text().content("$money\\").color(NamedTextColor.YELLOW).build())
                lore(listOf(text("Auction에서 사용할 수 있는 돈")))
            }
        }
    }

    fun create(player: Player, itemList: List<AuctionItem>): InvFrame {


        return InvFX.frame(1, text("Auction!")) {

            slot(0, 0) {
                item = moneyView(player)
            }

            list(2, 0, 6, 1, true, { itemList }) {
                transform {
                    it.itemStack.clone().apply {
                        itemMeta = itemMeta.apply {
                            lore(listOf(text().content("${it.prise}\\").color(NamedTextColor.WHITE)
                                .decoration(TextDecoration.ITALIC, false)
                                .build(), text("")) + if (it.owner == player.uniqueId) text().content("클릭으로 판매 취소")
                                .color(NamedTextColor.WHITE).build()
                            else text().content("쉬프트 클릭으로 장바구니 추가").color(NamedTextColor.LIGHT_PURPLE)
                                .decoration(TextDecoration.ITALIC, true).build())
                        }
                    }
                }

                onClickItem { _, _, (item, _), event ->
                    var isAvailableItem = false

                    Database.connection {
                        val statement = connection.createStatement()
                        val resultSet =
                            statement.executeQuery("select count(*) from item where item_key = ${item.itemKey}")

                        if (resultSet.next()) {
                            if (resultSet.getInt(1) == 1) {
                                isAvailableItem = true
                            }
                        }
                    }

                    if (isAvailableItem) {
                        if (item.owner == event.whoClicked.uniqueId) {
                            deleteItem(item)
                            event.whoClicked.inventory.addItem(item.itemStack)
                            event.whoClicked.closeInventory()

                            event.whoClicked.sendMessage("진행중인 판매가 취소되었습니다")
                        } else {
                            if (event.click == ClickType.SHIFT_LEFT || event.click == ClickType.SHIFT_RIGHT) {
                                val user = getUser(player.uniqueId)

                                Database.connection {
                                    prepareStatement("insert into shopping_bag(user_key, item_key) value(${user.userKey},${item.itemKey})") {
                                        executeUpdate()
                                    }
                                }

                                player.sendMessage("장바구니에 추가되었습니다")
                            } else {
                                var money = 0

                                Database.connection {
                                    val statement = connection.createStatement()
                                    val resultSet =
                                        statement.executeQuery("select money from user where uuid = \"${event.whoClicked.uniqueId}\"")

                                    if (resultSet.next()) {
                                        money = resultSet.getInt(1)
                                    }

                                    resultSet.close()
                                    statement.close()
                                }

                                if (item.prise <= money) {
                                    money -= item.prise
                                    //플레이어 돈 지불
                                    Database.connection { //차감
                                        connection.prepareStatement("update user set money = $money where uuid =\"${event.whoClicked.uniqueId}\"")
                                            .let {
                                                it.executeUpdate()
                                                it.close()
                                            }
                                    }

                                    Database.connection { // 추가
                                        connection.prepareStatement("update user set money = money + ${item.prise} where uuid=\"${item.owner}\"")
                                            .let {
                                                it.executeUpdate()
                                                it.close()
                                            }
                                    }

                                    deleteItem(item)

                                    event.whoClicked.inventory.addItem(item.itemStack)
                                    event.whoClicked.closeInventory()
                                } else {
                                    event.whoClicked.sendMessage("돈이 부족합니다\n 가격 -> ${item.prise} 현재 돈 -> $money")
                                    refresh()
                                }
                            }
                        }
                    } else {
                        event.whoClicked.sendMessage("이미 판매된 아이템 입니다")
                        refresh()
                    }
                }
            }.let { list ->

                slot(1, 0) {
                    item = previousButton
                    onClick { list.index-- }
                }

                slot(8, 0) {
                    item = nextButton
                    onClick { list.index++ }
                }
            }
        }
    }

    private fun deleteItem(item: AuctionItem) {
        Database.connection {
            connection.prepareStatement("delete from item where item_key = ${item.itemKey}").let {
                it.executeUpdate()
                it.close()
            }
        }
    }

    private fun getUser(uuid: UUID): AuctionUser {
        var auctionUser: AuctionUser? = null
        Database.connection {
            createStatement {
                executeQuery("select * from user where uuid = \"$uuid\"").let {
                    if (it.next()) {
                        auctionUser = AuctionUser(it.getInt("user_key"), uuid, it.getInt("money"))
                    }
                }
            }
        }
        requireNotNull(auctionUser)
        return auctionUser!!
    }
}
