import org.json.JSONObject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock

class GitHubNamingDataProcessorTest {

    // arrange
    private var queryProcessor: GitHubQueryProcessor = mock(GitHubQueryProcessor::class.java)
    private val processor = GitHubNamingDataProcessor(queryProcessor, "test_data.txt")


    @Test
    fun `processor converts JSONObject successfully`() {
        // arrange
        val jsonTestString =
            "{\"total_count\": 1, \"incomplete_results\": false, \"items\": [ {\"name\": \"Class.java\"} ] }"
        val testData = JSONObject(jsonTestString)
        Mockito.`when`(queryProcessor.queryGitHubAPI(Mockito.anyString(), Mockito.anyString())).thenReturn(testData)
        val expectedMap = HashMap<String, Int>()
        expectedMap["Class"] = 1
        val expectedResult = NamingData(8, expectedMap)

        //act
        val result = processor.processData("code_search_samples", true)

        //assert
        assertEquals(expectedResult, result)
    }
}
