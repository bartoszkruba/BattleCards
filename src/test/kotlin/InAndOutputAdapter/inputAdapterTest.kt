package InAndOutputAdapter

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class inputAdapterTest {

    @Test
    fun userNameInput() {
    }

    @Test
    fun deckNameInput() {
    }

    @Test
    fun userNameValidation(){
        var userInput = inputAdapter()

        var userName = userInput.userNameValidation(null)
        assertFalse(userName)

        userName = userInput.userNameValidation("name")
        assertTrue(userName)

        userName = userInput.userNameValidation("")
        assertFalse(userName)

        userName = userInput.userNameValidation("1234")
        assertFalse(userName)

        userName = userInput.userNameValidation("name77")
        assertFalse(userName)

        userName = userInput.userNameValidation("name more than nine characters")
        assertFalse(userName)
    }
}