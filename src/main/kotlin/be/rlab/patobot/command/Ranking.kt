package be.rlab.patobot.command

import be.rlab.patobot.domain.PlayersService
import be.rlab.patobot.domain.model.Player
import be.rlab.tehanu.annotations.*
import be.rlab.tehanu.messages.MessageContext
import be.rlab.tehanu.messages.TextTrigger.Companion.CONTAINS
import be.rlab.tehanu.messages.TextTrigger.Companion.NORMALIZE

@MessageListener("ranking")
class Ranking(
    private val playersService: PlayersService
) {

    companion object {
        const val SEARCH: String = "searching"
        const val DISPLAY_LEADERBOARD: String = "displayLeaderBoard"
    }

    @Handler
    @Triggers(
        Trigger([
            TriggerParam(CONTAINS, "@patobot", "@US034S5TQ")
        ]),
        Trigger([
            TriggerParam(CONTAINS, "ranking", "tabla", "posiciones", "venimos", "vamos", "torneo", "ganar"),
            TriggerParam(NORMALIZE, "true")
        ])
    )
    fun showRanking(context: MessageContext): MessageContext = with(context) {
        talk(messages[SEARCH])
        val players: List<Player> = playersService.listPlayers()
        val leaderBoard: String = players.subList(0, 10).mapIndexed{ index, player ->
            "${index + 1} - ${player.name}"
        }.joinToString("\n")

        talk(messages[DISPLAY_LEADERBOARD])
        talk(leaderBoard)
    }
}
