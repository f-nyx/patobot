package be.rlab.patobot.command

import be.rlab.patobot.domain.Constants.USER_SLACK
import be.rlab.patobot.domain.Constants.USER_TELEGRAM
import be.rlab.patobot.domain.RankingMonitor
import be.rlab.patobot.domain.model.Subscription
import be.rlab.tehanu.annotations.*
import be.rlab.tehanu.messages.MessageContext
import be.rlab.tehanu.messages.TextTrigger.Companion.CONTAINS
import be.rlab.tehanu.messages.TextTrigger.Companion.NORMALIZE

@MessageListener("monitor")
class Monitor(
    private val rankingMonitor: RankingMonitor
) {

    companion object {
        const val MONITOR_SUBSCRIBED: String = "monitorSubscribed"
        const val MONITOR_UNSUBSCRIBED: String = "monitorUnsubscribed"
        const val ALREADY_SUBSCRIBED: String = "alreadySubscribed"
        const val ALREADY_UNSUBSCRIBED: String = "alreadyUnsubscribed"
    }

    @Handler
    @Triggers(
        Trigger([
            TriggerParam(CONTAINS, USER_TELEGRAM, USER_SLACK)
        ]),
        Trigger([
            TriggerParam(CONTAINS, "monitor"),
            TriggerParam(NORMALIZE, "true")
        ]),
        Trigger([
            TriggerParam(CONTAINS, "prende", "encende", "empeza", "inicia", "arranca", "ponete"),
            TriggerParam(NORMALIZE, "true")
        ])
    )
    fun enableMonitor(context: MessageContext): MessageContext = with(context) {
        val subscription: Subscription = rankingMonitor.subscribe(context.chat)

        if (!subscription.subscribed) {
            talk(messages[MONITOR_SUBSCRIBED])
        } else {
            talk(messages[ALREADY_SUBSCRIBED])
        }
    }

    @Handler
    @Triggers(
        Trigger([
            TriggerParam(CONTAINS, USER_TELEGRAM, USER_SLACK)
        ]),
        Trigger([
            TriggerParam(CONTAINS, "monitor"),
            TriggerParam(NORMALIZE, "true")
        ]),
        Trigger([
            TriggerParam(CONTAINS, "apaga", "para", "detene", "deja"),
            TriggerParam(NORMALIZE, "true")
        ])
    )
    fun disableMonitor(context: MessageContext): MessageContext = with(context) {
        val subscription: Subscription = rankingMonitor.unsubscribe(context.chat)

        if (subscription.subscribed) {
            talk(messages[MONITOR_UNSUBSCRIBED])
        } else {
            talk(messages[ALREADY_UNSUBSCRIBED])
        }
    }
}
