import khttp.get
import org.json.JSONObject
import java.io.IOException

class GitHubQueryProcessor {

    /** Hardcoded constants */
    private val tokenFileName: String = "access_token.txt"
    private val searchApiUrl: String = "https://api.github.com/search/"

    /** Sends request to the GitHub search API and returns response.
     *
     * @param query the search query.
     * @param type specifies what type of object is searched, e.g repositories or code.
     * @return http response as JSON object.
     */
    fun queryGitHubAPI(query: String, type: String): JSONObject? {
        val token = getAccessToken(tokenFileName) ?: return null
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
     * @param fileName the file where the access token is contained
     * @return the token string.
     */
    fun getAccessToken(fileName: String): String? {
        var token: String? = null
        try {
            token = javaClass.getResource(fileName).readText()
        } catch (e: IllegalStateException) {
            println("Please create and place a personal access GitHub token in $fileName.")
            e.printStackTrace()
        }
        return token
    }
}