package be.rlab.patobot.domain

import be.rlab.patobot.domain.model.Player
import be.rlab.patobot.services.airtable.AirTableClient

/** Service to manage players.
 */
class PlayersService(
    private val airTableClient: AirTableClient
) {
    /** Returns the full list of players.
     */
    fun listPlayers(): List<Player> {
        return airTableClient.listPlayers()
    }
}
