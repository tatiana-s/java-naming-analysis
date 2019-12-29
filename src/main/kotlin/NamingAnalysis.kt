fun main() {
    val queryProcessor = GitHubQueryProcessor()
    val dataProcessor = GitHubNamingDataProcessor(queryProcessor, "saved_data.txt")
    val result = dataProcessor.processData("code_search_samples", true)
    print(result)
}