package be.rlab.patobot

import be.rlab.patobot.config.ApplicationBeans
import be.rlab.patobot.domain.RankingMonitor
import be.rlab.tehanu.SpringApplication
import be.rlab.tehanu.config.SlackBeans
import be.rlab.tehanu.config.TelegramBeans
import org.springframework.beans.factory.getBean

class Main : SpringApplication() {

    override fun initialize() {
        applicationContext.apply {
            ApplicationBeans.beans(resolveConfig()).initialize(this)
            TelegramBeans.beans(resolveConfig()).initialize(this)
            SlackBeans.beans(resolveConfig()).initialize(this)
        }
    }

    override fun ready() {
        val monitor: RankingMonitor = applicationContext.getBean()
        monitor.start()
    }
}

fun main() {
    Main().start()
}
