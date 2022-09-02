package io.github.changwook987.auction.command

import io.github.monun.kommand.PluginKommand
import net.kyori.adventure.text.Component.text

object KommandAuction {
    fun register(kommand: PluginKommand) {
        kommand.apply {
            register("auction", "옥션") {
                requires { isPlayer }

                executes {
                    val player = player

                    feedback(text("사용하기위해서 로그인을 해주세요"))
                }
            }

            register("login", "로그인") {
                requires { isPlayer }

                executes {

                }
            }

            register("register", "회원가입") {
                requires { isPlayer }

                executes {

                }
            }
        }
    }
}