import org.json.JSONArray
import org.json.JSONObject

class GitHubNamingDataProcessor(private val queryProcessor: GitHubQueryProcessor) {

    /** Collects and processes data according to specified way.
     * At the moment only one way to get data from GitHub is supported, see README.md for more information.
     *
     * @return HashMap mapping a java class name to the number of occurrences on GitHub.
     */
    fun processData(sampleType: String): HashMap<String, Int>? {
        return if (sampleType == "code_search_samples") {
            convertDataToMapCodeSearchSamples(collectDataCodeSearchSamples())
        } else {
            print("Other types of sampling are not supported yet. Try \"code_search_samples\"")
            null
        }
    }

    /** Queries data to get code search samples.
     *
     * @return list of JSONObjects resulting from query.
     */
    private fun collectDataCodeSearchSamples(): ArrayList<JSONObject> {
        val fileSizes = arrayOf(100, 250, 500, 1000, 2500, 5000, 10000)
        val resultList = arrayListOf<JSONObject>()
        for (size in fileSizes) {
            // Query searches for java files of the specified file size containing the word "class"
            val result =
                queryProcessor.queryGitHubAPI("class+in:file+extension:java+size:$size&sort=indexed", "code")
                    ?: continue
            resultList.add(result)
        }
        return resultList
    }

    /** QConverts collected data to map recording occurrences of a name on GitHub.
     *
     * @todo figure out why number of processed results so low, json error handling, check for file duplicates (potentially map of objects and not just names)
     * @param data github data as list of json objects.
     * @return map of names and occurrences.
     */
    private fun convertDataToMapCodeSearchSamples(data: ArrayList<JSONObject>): HashMap<String, Int> {
        val resultsMap = hashMapOf<String, Int>()
        for (d in data) {
            val items: JSONArray = d.getJSONArray("items")
            println(items.length())
            for (i in 0 until items.length()) {
                val name = items.getJSONObject(i).get("name") ?: continue
                println(name)
                val nameString = (name as String).substringBefore(".")
                if (resultsMap.containsKey(name)) {
                    resultsMap[nameString] = resultsMap.getValue(nameString) + 1
                } else {
                    resultsMap[nameString] = 1
                }
            }
        }
        return resultsMap
    }
}