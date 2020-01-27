package be.rlab.patobot.domain.model

/** Represents a Duck player.
 */
data class Player(
    /** Unique id for this player. */
    val id: String,
    /** Player's display name. */
    val name: String,
    /** General rating. */
    val rating: Double,
    /** Player's hat image url. */
    val hatUrl: String?
)