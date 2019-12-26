fun main() {
    val queryProcessor = GitHubQueryProcessor()
    val dataProcessor = GitHubNamingDataProcessor(queryProcessor)
    dataProcessor.processData("code_search_samples")
}