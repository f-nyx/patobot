package be.rlab.patobot.config

import be.rlab.patobot.command.Monitor
import be.rlab.patobot.command.Players
import be.rlab.patobot.command.Ranking
import be.rlab.patobot.domain.PlayersService
import be.rlab.patobot.domain.RankingMonitor
import be.rlab.patobot.services.airtable.AirTableClient
import be.rlab.patobot.services.airtable.ClientConfig
import be.rlab.tehanu.messages.Client
import com.typesafe.config.Config
import org.springframework.context.support.beans

object ApplicationBeans {

    fun beans(config: Config) = beans {
        // Listeners
        bean<Monitor>()
        bean<Ranking>()
        bean<Players>()

        bean<PlayersService>()
        bean {
            RankingMonitor(
                memory = ref(),
                playersService = ref(),
                clients = provider<Client>().toList()
            )
        }
        bean {
            val airtableConfig: Config = config.getConfig("airtable")
            val clientConfig = ClientConfig(
                accessToken = airtableConfig.getString("access-token"),
                appId = airtableConfig.getString("app-id"),
                apiUrl = airtableConfig.getString("api-url")
            )

            AirTableClient(clientConfig)
        }
    }
}
