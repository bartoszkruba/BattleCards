package inAndOutputAdapter

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class InputTest {

    @Test
    fun userNameInput() {
    }

    @Test
    fun deckNameInput() {
        var userInput = Input()
        val decks = listOf("one", "two", "three", "four")

        assertTrue(userInput.deckNameInput(decks.get(0)))
        assertTrue(userInput.deckNameInput(decks.get(3)))
        assertFalse(userInput.deckNameInput("deck name"))
        assertFalse(userInput.deckNameInput(null))
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