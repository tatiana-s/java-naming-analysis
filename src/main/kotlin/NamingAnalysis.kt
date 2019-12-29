fun main() {
    val queryProcessor = GitHubQueryProcessor()
    val dataProcessor = GitHubNamingDataProcessor(queryProcessor, "saved_data.txt")
    val result = dataProcessor.processData("code_search_samples", false)
    if (result != null) {
        println(result)
        println(getCount("Item", result))
        println(getCount("HelloWorld", result))
        println(getCountContained("Test", result))
        println(getCountContained("My", result))
        println(getCountContained("Class", result))
        println(getMaxCount(result))
    }
}

/** Return the number of occurrences for a name if it exists in the data.
 *
 * @param name choose how to get data from the GitHub API.
 * @param data the naming data.
 * @return the number of occurrences.
 */
fun getCount(name: String, data: NamingData): Int? {
    return if (data.data.containsKey(name)) {
        data.data[name]
    } else {
        println("Data doesn't contain $name.")
        0
    }
}

/** Return the number of occurrences for string if it exists in the data as part of a name.
 *
 * @param text choose how to get data from the GitHub API.
 * @param data the naming data.
 * @return the number of occurrences.
 */
fun getCountContained(text: String, data: NamingData): Int? {
    val items = data.data
    var count = 0;
    for ((k, v) in items) {
        if (k.contains(text)) {
            count += v
        }
    }
    return count
}

/** Return the name with the most occurrences in the data.
 *
 * @param data the naming data.
 * @return the number of occurrences.
 */
fun getMaxCount(data: NamingData): String {
    val items = data.data
    var maxCount = 0
    var maxCountName = ""
    for ((k, v) in items) {
        if (v > maxCount) {
            maxCount = v
            maxCountName = k
        }
    }
    if (maxCount < 2) {
        maxCountName = "No multiple occurrences of names."
    }
    return maxCountName
}