package be.rlab.patobot.domain.model

import be.rlab.tehanu.messages.model.Chat

data class Subscription(
    val chat: Chat,
    val subscribed: Boolean
) {
    fun unsubscribe(): Subscription = copy(
        subscribed = false
    )

    fun subscribe(): Subscription = copy(
        subscribed = true
    )
}
