import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class GitHubQueryProcessorTest {

    // arrange
    private val processor = GitHubQueryProcessor()

    @Test
    fun `getAccessToken returns token if file exists`() {
        // act
        val token = processor.getAccessToken("test_token.txt")
        // assert
        assertEquals("test_token", token)
    }

    @Test
    fun `getAccessToken catches exception if file doesn't exist`() {
        // act
        val token = processor.getAccessToken("invalid_token.txt")
        // assert
        assertEquals(null, token)
    }

}