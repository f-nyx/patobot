package be.rlab.patobot.services.airtable

import be.rlab.patobot.domain.model.Player
import be.rlab.tehanu.support.ObjectMapperFactory
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.OkHttpClient
import okhttp3.Request

class AirTableClient(
    private val config: ClientConfig
) {
    companion object {
        const val PLAYERS_TABLE: String = "Players"
    }

    private val client = OkHttpClient()
    private val objectMapper: ObjectMapper = ObjectMapperFactory.camelCaseMapper

    fun listPlayers(): List<Player> {
        return client.newCall(
            get(PLAYERS_TABLE).build()
        ).execute().use { response ->
            val jsonTable: JsonNode = objectMapper.readTree(response.body?.string())

            jsonTable["records"].map { jsonRecord ->
                Player(
                    id = jsonRecord["id"].textValue(),
                    name = jsonRecord["fields"]["name"].textValue(),
                    rating = jsonRecord["fields"]["rating"].doubleValue(),
                    hatUrl = jsonRecord["fields"]["hat"]?.let { hat ->
                        hat[0]["url"].textValue()
                    }
                )
            }.sortedByDescending { it.rating }
        }
    }

    private fun get(tableName: String): Request.Builder {
        val apiUrl: String = config.apiUrl.removeSuffix("/")

        return Request.Builder()
            .addHeader("Authorization", "Bearer ${config.accessToken}")
            .url("$apiUrl/${config.appId}/$tableName")
    }
}
