import khttp.get
import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class GitHubQueryProcessor {

    /** Hardcoded constants */
    private val tokenFileName: String = "access_token.txt"
    private val searchApiUrl: String = "https://api.github.com/search/"

    /** Sends request to the GitHub search API and returns response.
     *
     * @param query the search query
     * @param type specifies what type of object is searched, e.g repositories or code
     * @return http response as JSON object.
     */
    fun queryGitHubAPI(query: String, type: String): JSONObject? {
        val token = getAccessToken() ?: return null
        val url = "$searchApiUrl$type?q=$query"
        var response: JSONObject? = null
        try {
            response = get(url, headers = mapOf("Authorization" to "token $token")).jsonObject
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return response
    }

    /** Attempts to read GitHub OAuth access token from specified resource file.
     *
     * @return the token string.
     */
    private fun getAccessToken(): String? {
        var token: String? = null
        try {
            token = javaClass.getResource(tokenFileName).readText()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
        return token
    }

    /** URL extension function for GET http requests.
     *
     * @return http response as string.
     */
    private fun URL.getText(): String {
        return openConnection().run {
            this as HttpURLConnection
            inputStream.bufferedReader().readText()
        }
    }
}