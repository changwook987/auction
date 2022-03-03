package io.github.changwook987.auction

import org.bukkit.inventory.ItemStack
import java.util.*

data class AuctionItem(val itemKey: Int, val itemStack: ItemStack, val prise: Int, val owner: UUID)