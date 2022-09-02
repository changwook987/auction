package io.github.changwook987.auction.server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AuctionServerApplication

fun main(args: Array<String>) {
    runApplication<AuctionServerApplication>(*args)
}