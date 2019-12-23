import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class GitHubQueryProcessor {

    /** Sends request to the GitHub search API and returns response.
     *
     * @param query the search query
     * @param type specifies what type of object is searched, e.g repositories or code
     * @return http response as JSON string.
     */
    fun getResponse(query: String, type: String): String? {
        val fullQuery = "https://api.github.com/search/$type?q=$query"
        println(fullQuery)
        var response: String? = null
        try {
            response = URL(fullQuery).getText()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return response
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