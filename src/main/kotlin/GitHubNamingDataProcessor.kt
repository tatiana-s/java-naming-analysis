import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

class GitHubNamingDataProcessor(private val queryProcessor: GitHubQueryProcessor, private val dataFileName: String) {

    /** Collects and processes data according to specified way, returning the result and saving it in a file.
     * At the moment only one way to get data from GitHub is supported, see README.md for more information.
     *
     * @param sampleType choose how to get data from the GitHub API.
     * @param update if true, query the API to get data, else get saved results if they exist.
     * @return HashMap mapping a java class name to the number of occurrences on GitHub.
     */
    fun processData(sampleType: String, update: Boolean): NamingData? {
        var result: NamingData? = null
        if (sampleType == "code_search_samples") {
            val gson = Gson()
            val file = File(dataFileName)
            if (update || !file.exists()) {
                result = convertDataCodeSearchSamples(collectDataCodeSearchSamples())
                val jsonString = gson.toJson(result)
                file.createNewFile()
                file.writeText(jsonString)
            } else {
                result = gson.fromJson(file.readText(), NamingData::class.java)
            }
        } else {
            print("Other types of sampling are not supported yet. Try \"code_search_samples\".")
        }
        return result
    }

    /** Queries data to get code search samples.
     *
     * @return list of JSONObjects resulting from query.
     */
    private fun collectDataCodeSearchSamples(): ArrayList<JSONObject> {
        val fileSizes = arrayOf(100, 250, 500, 1000, 1500, 2500, 5000, 10000)
        val resultList = arrayListOf<JSONObject>()
        for (size in fileSizes) {
            // Query searches for java files of the specified file size containing the word "class that are most recently indexed"
            val result =
                queryProcessor.queryGitHubAPI(
                    "class+in:file+extension:java+size:$size&sort=indexed&per_page=50",
                    "code"
                )
                    ?: continue
            resultList.add(result)
        }
        return resultList
    }

    /** Converts collected data to map recording occurrences of a name on GitHub.
     *
     * @param data github data as list of json objects.
     * @return map of names and occurrences as NamingData object.
     */
    private fun convertDataCodeSearchSamples(data: ArrayList<JSONObject>): NamingData {
        val results = NamingData(0, HashMap())
        for (d in data) {
            // get items array with results
            try {
                val items: JSONArray = d.getJSONArray("items")
                results.count = results.count + items.length()
                // for each item get the name and either update the counter or create a new entry
                for (i in 0 until items.length()) {
                    val name = items.getJSONObject(i).get("name") ?: continue
                    // remove .java extension from name
                    val nameString = (name as String).substringBefore(".")
                    if (results.data.containsKey(name)) {
                        results.data[nameString] = results.data.getValue(nameString) + 1
                    } else {
                        results.data[nameString] = 1
                    }
                }
            } catch (e: org.json.JSONException) {
                println("Error occurred during query ($d)")
            }
        }
        return results
    }
}