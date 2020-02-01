package be.rlab.patobot.domain

import be.rlab.patobot.domain.model.Player
import be.rlab.patobot.domain.model.Subscription
import be.rlab.tehanu.messages.Client
import be.rlab.tehanu.messages.model.Chat
import be.rlab.tehanu.store.Memory
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.concurrent.timer

class RankingMonitor(
    memory: Memory,
    private val playersService: PlayersService,
    private val clients: List<Client>
) {

    companion object {
        private const val TIMER_NAME: String = "RankingMonitorTimer"
        private const val PERIOD: Long = 10000
        private const val SUBSCRIPTIONS_SLOT: String = "monitor::subscriptions"
    }

    private val logger: Logger = LoggerFactory.getLogger(RankingMonitor::class.java)
    private var ranking: List<Player> = emptyList()
    private var subscriptions: List<Subscription> by memory.slot(SUBSCRIPTIONS_SLOT, listOf<Subscription>())

    fun start() = timer(TIMER_NAME, daemon = true, period = PERIOD) {
        try {
            val players = playersService.listPlayers()

            if (ranking.isEmpty()) {
                ranking = players
            } else {
                checkChanges(players)
            }
        } catch (cause: Exception) {
            logger.error("error verifying players", cause)
        }
    }

    fun subscribe(chat: Chat): Subscription {
        val subscription: Subscription = subscriptions.find { subscription ->
            subscription.chat.id == chat.id
        } ?: Subscription(chat, subscribed = false)

        replace(subscription.subscribe())

        return subscription
    }

    fun unsubscribe(chat: Chat): Subscription {
        val subscription: Subscription = subscriptions.find { subscription ->
            subscription.chat.id == chat.id
        } ?: Subscription(chat, subscribed = false)

        replace(subscription.unsubscribe())

        return subscription
    }

    private fun replace(subscription: Subscription) {
        subscriptions = subscriptions.filter { existing ->
            existing.chat.id != subscription.chat.id
        }
        subscriptions = subscriptions + subscription
    }

    private fun checkChanges(players: List<Player>) {
        if (ranking.size != players.size) {
            notifyRanking()
            return
        }

        val hasChanges: Boolean = ranking.filterIndexed { index, player ->
            player.id != players[index].id
        }.isNotEmpty()

        if (hasChanges) {
            ranking = players
            notifyRanking()
        }
    }

    private fun notifyRanking() {
        subscriptions.forEach { subscription ->
            val client: Client = clients.find { client ->
                client.name == subscription.chat.clientName
            } ?: throw RuntimeException("Client not found: ${subscription.chat.clientName}")

            show(client, subscription.chat)
        }
    }

    private fun show(
        client: Client,
        chat: Chat
    ) {
        val leaderBoard: String = ranking.subList(0, 10).mapIndexed{ index, player ->
            "${index + 1} - ${player.name}"
        }.joinToString("\n")
        client.sendMessage(chat.id, leaderBoard)
    }
}
