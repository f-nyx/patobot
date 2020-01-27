package be.rlab.patobot.services.airtable

/** Airtable client configuration.
 */
data class ClientConfig(
    /** Access token to authenticate API calls. */
    val accessToken: String,
    /** Id to scope the API calls. */
    val appId: String,
    /** Base url of the Airtable API. */
    val apiUrl: String
)
