package be.rlab.patobot.command

import be.rlab.patobot.domain.PlayersService
import be.rlab.patobot.domain.model.Player
import be.rlab.tehanu.annotations.*
import be.rlab.tehanu.messages.MessageContext
import be.rlab.tehanu.messages.TextTrigger.Companion.CONTAINS
import be.rlab.tehanu.messages.TextTrigger.Companion.NORMALIZE

@MessageListener("ranking")
class Players(
    private val playersService: PlayersService
) {

    companion object {
        const val SEARCH: String = "searching"
    }

    @Handler
    @Triggers(
        Trigger([
            TriggerParam(CONTAINS, "@patobot", "@US034S5TQ")
        ]),
        Trigger([
            TriggerParam(CONTAINS, "lista"),
            TriggerParam(NORMALIZE, "true")
        ]),
        Trigger([
            TriggerParam(CONTAINS, "jugadores", "participantes", "inscriptas"),
            TriggerParam(NORMALIZE, "true")
        ])
    )
    fun showPlayers(context: MessageContext): MessageContext = with(context) {
        talk(messages[SEARCH])
        val players: List<Player> = playersService.listPlayers().sortedBy { player ->
            player.name.toLowerCase()
        }

        talk(players.joinToString("\n") { player ->
            player.name
        })
    }
}
