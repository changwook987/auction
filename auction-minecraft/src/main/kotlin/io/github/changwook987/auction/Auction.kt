package io.github.changwook987.auction

object Auction {
    lateinit var manager: AuctionManager
        private set

    fun init(
        manager: AuctionManager
    ) {
        this.manager = manager
    }
}