package inAndOutputAdapter

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class InputTest {

    @Test
    fun userNameInput() {
    }

    @Test
    fun deckNameInput() {
    }

    @Test
    fun userNameValidation() {
        var userInput = Input()

        assertFalse(userInput.userNameValidation(null))

        assertTrue(userInput.userNameValidation("name"))

        assertFalse(userInput.userNameValidation(""))

        assertFalse(userInput.userNameValidation("1234"))

        assertFalse(userInput.userNameValidation("name77"))

        assertFalse(userInput.userNameValidation("name more than nine characters"))
    }
}