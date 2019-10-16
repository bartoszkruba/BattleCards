package inAndOutputAdapter

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class InputTest {

    @Test
    fun userNameInput() {
        var userInput = Input()

        assertFalse(userInput.userNameValidation(null))
        assertTrue(userInput.userNameValidation("name"))
        assertFalse(userInput.userNameValidation(""))
        assertFalse(userInput.userNameValidation("1234"))
        assertFalse(userInput.userNameValidation("name77"))
        assertFalse(userInput.userNameValidation("name more than nine characters"))
    }

    @Test
    fun deckNameInput() {
        var userInput = Input()
        val decks = listOf("one", "two", "three", "four")

        assertTrue(userInput.deckNameInput(decks.get(0)))
        assertTrue(userInput.deckNameInput(decks.get(3)))
        assertFalse(userInput.deckNameInput("deck name"))
        assertTrue(userInput.deckNameInput("1"))
        assertFalse(userInput.userNameInput("7"))
        assertFalse(userInput.deckNameInput(null))
    }
}